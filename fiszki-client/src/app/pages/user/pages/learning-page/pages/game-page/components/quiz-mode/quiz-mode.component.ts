import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { Observable, Subscription } from 'rxjs';
import { BaseFlashcardInterface } from 'src/app/shared/models/flashcard.interface';
import { GameSettingsState } from '../../../../store/game.state';
import { QuizItemInterface } from './models/quiz-item.model';

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

  constructor(private store : Store<{gameSettings : GameSettingsState}>, private router : Router){}

  ngOnInit(): void 
  {
    this.gameSettings$ = this.store.select('gameSettings');

    this.gameSettingsSubscription = this.gameSettings$
      .subscribe(data => {
        this.flashcards = data.flashcards;
        this.polishFirst = data.polishFirst;
      })

      // if(this.flashcards.length < 4)
      // {
      //   this.router.navigate(['/user/learning']);
      //   return
      // }

      this.CreateQuiz(this.flashcards);
      console.log(this.quiz);

  }

  ngOnDestroy() : void 
  {
    if (this.gameSettingsSubscription) {
      this.gameSettingsSubscription.unsubscribe();
    }
  }

  private CreateQuiz(flashcards : BaseFlashcardInterface[]) : void
  {
    flashcards.forEach(flashcard => {
      const quizItem : QuizItemInterface[] = [];
      
      const question = this.polishFirst === true ? flashcard.word : flashcard.translatedWord;
      const correctAnswer = this.polishFirst === true ? flashcard.translatedWord : flashcard.word;
      const wariants = this.CreateWariants(flashcards, correctAnswer);

      const item : QuizItemInterface = {
        question : question,
        correctAnswer : correctAnswer,
        wariants : this.ShuffleArray(wariants)
      }

      this.quiz.push(item);
    })
  }

  private CreateWariants(flashcards : BaseFlashcardInterface[], correctAnswer : string) : string[]
  {
    const wariants : string [] = [correctAnswer];
    for(let i = 0; i < 3; i++)
    {
      let randomIndex;
      let randomWord;
      if(this.polishFirst === true)
      {
          randomIndex = Math.floor(Math.random() * flashcards.length);
          randomWord = flashcards[randomIndex].translatedWord;

          do
          {
              randomIndex = Math.floor(Math.random() * flashcards.length);
              randomWord = flashcards[randomIndex].translatedWord;
          } while(correctAnswer.includes(randomWord) || wariants.includes    (randomWord))
      }
      else
      {
          randomIndex = Math.floor(Math.random() * flashcards.length);
          randomWord = flashcards[randomIndex].word;

          do
          {
              randomIndex = Math.floor(Math.random() * flashcards.length);
              randomWord = flashcards[randomIndex].word;
          } while(correctAnswer.includes(randomWord) || wariants.includes(randomWord))  
      }
      wariants.push(randomWord);
    }
    return wariants;
  }

  private ShuffleArray(wariants : string[]) : string[]
  {
    for(let i = wariants.length - 1; i > 0; i--)
    {
      let j = Math.floor(Math.random() * (i + 1));
      [wariants[i], wariants[j]] = [wariants[j], wariants[i]];
    }
    return wariants;
  }

}
