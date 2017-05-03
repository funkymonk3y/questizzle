import {Component, OnInit, Input} from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { QuestionService } from './question.service';
import {EditAssessmentComponent} from "../assessment/edit-assessment.component";

@Component({
  selector: 'edit-question-search-popup',
  templateUrl: './edit-search-popup.component.html'
})
export class EditSearchPopupComponent implements OnInit {

  private form: FormGroup;
  private questions = [];
  private portalId: string;

  constructor(
    private editAssessmentComponent: EditAssessmentComponent,
    private formBuilder: FormBuilder,
    private questionService: QuestionService,
  ) {}

  ngOnInit(): void {
    this.portalId = this.editAssessmentComponent.getPortalId();

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
    this.editAssessmentComponent.addQuestion(this.questions[questionIndex]);
  }
}
