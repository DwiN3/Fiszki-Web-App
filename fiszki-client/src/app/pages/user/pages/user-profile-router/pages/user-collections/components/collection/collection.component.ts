import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-collection',
  templateUrl: './collection.component.html',
  styleUrls: ['./collection.component.scss']
})
export class CollectionComponent {
    @Input() collectionName : string = '';
    @Input() flashcardsQuantity : number = 0;

}
