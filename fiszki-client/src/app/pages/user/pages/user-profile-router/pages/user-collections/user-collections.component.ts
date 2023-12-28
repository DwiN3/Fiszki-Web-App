import { Component, OnInit } from '@angular/core';
import { UserCollectionService } from 'src/app/pages/user/services/user-collection.service';

@Component({
  selector: 'app-user-collections',
  templateUrl: './user-collections.component.html',
  styleUrls: ['./user-collections.component.scss']
})
export class UserCollectionsComponent implements OnInit{
  
  constructor(private userCollectionService : UserCollectionService){}

  ngOnInit(): void {
      this.userCollectionService.GetCollections()
        .subscribe(res => {
          console.log(res);
        }, err => {
          console.log(err)
        })
  }

}
