import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddFlashcardComponent } from './add-flashcard.component';

describe('AddFlashcardComponent', () => {
  let component: AddFlashcardComponent;
  let fixture: ComponentFixture<AddFlashcardComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddFlashcardComponent]
    });
    fixture = TestBed.createComponent(AddFlashcardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
