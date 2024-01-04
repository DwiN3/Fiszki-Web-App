import { Component, OnInit } from '@angular/core';
import { faRepeat, faArrowRight } from '@fortawesome/free-solid-svg-icons';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { setLanguage, setLearningMode } from '../../store/game-settings.action';
import { GameSettingsState } from '../../store/game.state';

@Component({
  selector: 'app-learning-page-settings',
  templateUrl: './learning-page-settings.component.html',
  styleUrls: ['./learning-page-settings.component.scss']
})
export class LearningPageSettingsComponent implements OnInit{

  faRepeat = faRepeat;
  faArrowRight = faArrowRight;

  gameSettings$! : Observable<GameSettingsState>

  selectedMode : string = '';
  polishFirst : boolean | null = null; 

  constructor(private store : Store<{gameSettings : GameSettingsState}>){}

  ngOnInit(): void 
  {
    this.gameSettings$ = this.store.select('gameSettings');
    this.gameSettings$
      .subscribe(data => {
        this.selectedMode = data.learningMode;
        this.polishFirst = data.polishFirst;
      })
  }

  ChangeLanguage() : void
  {
    this.store.dispatch(setLanguage());
  }

  ChangeMode() : void
  {
    this.store.dispatch(setLearningMode({mode : this.selectedMode}));
  }

}
