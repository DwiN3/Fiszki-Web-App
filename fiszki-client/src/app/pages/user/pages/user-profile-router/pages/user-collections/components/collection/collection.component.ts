import { Component, Input, ViewChild } from '@angular/core';
import { faPlusCircle, faTrash, faEdit } from '@fortawesome/free-solid-svg-icons';
import { Store } from '@ngrx/store';
import { PlaceholderDirective } from 'src/app/shared/ui/alert/directive/placeholder.directive';
import { AlertService } from 'src/app/shared/ui/alert/service/alert.service';
import { CarouselState } from '../../store/carousel.state';
import { CollectionsState } from '../../store/collections.state';

@Component({
  selector: 'app-collection',
  templateUrl: './collection.component.html',
  styleUrls: ['./collection.component.scss']
})
export class CollectionComponent {

    @Input() collectionName : string = '';
    @Input() flashcardsQuantity : number = 0;

    @ViewChild(PlaceholderDirective, { static: true }) alertHost!: PlaceholderDirective;

    faPlusCircle = faPlusCircle;
    faTrash = faTrash;
    faEdit = faEdit;

    constructor(private alertService : AlertService, private store : Store<{collections : CollectionsState}>, private carouselStore : Store<{carousel : CarouselState}>){}

    OnDelete() : void
    {
        
    }

}
