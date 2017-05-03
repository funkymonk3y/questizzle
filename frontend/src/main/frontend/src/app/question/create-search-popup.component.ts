import {Component, OnInit, Input} from '@angular/core';
import { CreateAssessmentComponent } from '../assessment/create-assessment.component';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { QuestionService } from './question.service';

@Component({
  selector: 'create-question-search-popup',
  templateUrl: './create-search-popup.component.html'
})
export class CreateSearchPopupComponent implements OnInit {

  private form: FormGroup;
  private questions = [];
  private portalId: string;

  constructor(
    private createAssessmentComponent: CreateAssessmentComponent,
    private formBuilder: FormBuilder,
    private questionService: QuestionService,
  ) {}

  ngOnInit(): void {
    this.portalId = this.createAssessmentComponent.getPortalId();

    this.form = this.formBuilder.group({
      query: this.formBuilder.control('', Validators.required)
    });
  }

  performSearch(formSubmission) {
    this.questionService.searchQuestions(this.portalId, formSubmission['query'])
      .subscribe(searchResults => {
        this.questions = searchResults;
      });
  }

  addQuestion(questionIndex:number) {
    this.createAssessmentComponent.addQuestion(this.questions[questionIndex]);
  }
}
