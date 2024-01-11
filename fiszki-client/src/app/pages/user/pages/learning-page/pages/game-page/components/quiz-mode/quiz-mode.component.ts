import { ThisReceiver } from '@angular/compiler';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { ignoreElements, Observable, Subscription, timer } from 'rxjs';
import { BaseFlashcardInterface } from 'src/app/shared/models/flashcard.interface';
import { GameSettingsState } from '../../../../store/game.state';
import { QuizItemInterface } from './models/quiz-item.model';
import { CreateQuizService } from './service/create-quiz-service';

@Component({
  selector: 'app-quiz-mode',
  templateUrl: './quiz-mode.component.html',
  styleUrls: ['./quiz-mode.component.scss']
})
export class QuizModeComponent implements OnInit, OnDestroy{
  
  gameSettings$! : Observable<GameSettingsState>
  private gameSettingsSubscription: Subscription | undefined;

  flashcards : BaseFlashcardInterface[] = [];
  round : number = 0;
  quiz : QuizItemInterface[] = [];
  polishFirst : boolean = false;
  points : number = 0;

  timer : any | null = null;
  intervalValue : number = 4000;
  isClicked : boolean = false;
  timerWidthInterval : any | null = null;
  timerWidth : number = 0;

  constructor(private store : Store<{gameSettings : GameSettingsState}>, private router : Router, private quizService : CreateQuizService){}

  ngOnInit(): void 
  {
    this.gameSettings$ = this.store.select('gameSettings');

    this.gameSettingsSubscription = this.gameSettings$
      .subscribe(data => {
        this.flashcards = data.flashcards;
        this.polishFirst = data.polishFirst;
      })

      if(this.flashcards.length < 4)
      {
        this.router.navigate(['/user/learning']);
        return
      }

      this.quiz = this.quizService.CreateQuiz(this.flashcards, this.polishFirst);
      
      this.SetTimer();
      this.SetWidthTimer();
  }

  ngOnDestroy() : void 
  {
    if (this.gameSettingsSubscription) {
      this.gameSettingsSubscription.unsubscribe();
    }
  }

  private SetTimer(): void 
  {
    this.timer = setInterval(() => {

      if (this.round + 1 === this.quiz.length) {
        clearInterval(this.timer);
        clearInterval(this.timerWidthInterval);
        return;
      }
      
      this.round++;
      this.timerWidth = 0;

      this.SetWidthTimer();

    }, this.intervalValue);
  }

  private SetWidthTimer() : void
  {
    this.timerWidthInterval = setInterval(() => {
      if(this.timerWidth >= 392 || this.round === this.quiz.length)
      {
        clearInterval(this.timerWidthInterval);
        return
      }
      this.timerWidth += 1;
    }, 10)
  }

  ResetTimers() : void
  {
    clearInterval(this.timer);
    clearInterval(this.timerWidthInterval);

    if(this.quiz.length > this.round + 1)
    {
      setTimeout(() => {
        this.timerWidth = 0;
        this.round++;
        this.SetWidthTimer();
        this.SetTimer();
      }, 1000)
    }

  }

  private ResetInterval() : void
  {
    clearInterval(this.timer);
    this.SetTimer();
  }

}
