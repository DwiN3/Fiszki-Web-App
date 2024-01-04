import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable, Subscription } from 'rxjs';
import { FlashcardService } from 'src/app/pages/user/services/user.flashcards.service';
import { GameSettingsState } from '../../store/game.state';

@Component({
  selector: 'app-game-page',
  templateUrl: './game-page.component.html',
  styleUrls: ['./game-page.component.scss']
})
export class GamePageComponent implements OnInit{
  
  constructor(private store : Store<{gameSettings : GameSettingsState}>, private flashcardService : FlashcardService){}

  gameSettings$! : Observable<GameSettingsState>
  private gameSettingsSubscription: Subscription | undefined;

  learningMode : boolean | null = null;
  categoryName : string = ''
  limit : number = 0

  ngOnInit(): void 
  {
    this.gameSettings$ = this.store.select('gameSettings');
    this.gameSettingsSubscription = this.gameSettings$
      .subscribe(data => {
        if(data.learningMode === 'learning')
          this.learningMode = true;
        else this.learningMode = false;
        this.categoryName = data.category;
        this.limit = data.limit;
      })

    this.flashcardService.GetFlashcardsByCategory(this.categoryName, this.limit)
      .subscribe(data => {
        console.log(data);
      })
  }

  ngOnDestroy(): void 
  {
    if (this.gameSettingsSubscription) {
      this.gameSettingsSubscription.unsubscribe();
    }
  }


}
