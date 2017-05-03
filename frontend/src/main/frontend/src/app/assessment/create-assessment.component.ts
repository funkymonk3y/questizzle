import { Component, OnInit, OnDestroy } from '@angular/core';
import { Validators, FormBuilder, FormGroup, FormArray, FormControl } from '@angular/forms';

import { AssessmentService } from './assessment.service';
import { ActivatedRoute, Router } from '@angular/router';
import {PortalService} from "../portal/portal.service";

@Component({
  selector: 'create-assessment',
  templateUrl: './create-assessment.component.html'
})
export class CreateAssessmentComponent implements OnInit, OnDestroy {

  private form: FormGroup;
  private questions = [];
  private portalId: string;
  private pathParamSub: any;
  private portalInfo: any = null;

  private topicNotChosenError:boolean = false;

  constructor(
    private formBuilder: FormBuilder,
    private portalService: PortalService,
    private assessmentService: AssessmentService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.pathParamSub = this.route.params.subscribe(params => {
      this.portalId = params['id'];
      this.getPortalInfo(this.portalId);
    });

    this.form = this.formBuilder.group({
      name: this.formBuilder.control('', Validators.required),
      instructions: this.formBuilder.control('')
    });
  }

  ngOnDestroy(): void {
    this.pathParamSub.unsubscribe();
  }

  validateFormData(form) {
    let result:boolean = false;

    let atLeastOneTopicChosen:boolean = false;

    for(let i = 0; i < this.portalInfo.topics.length; i++) {
      let checked = form['topic_' + i];

      if(checked) {
        atLeastOneTopicChosen = true;
      }
    }

    if(atLeastOneTopicChosen == false) {
      this.topicNotChosenError = true;
      result = true;
    }

    return result;
  }

  onFormSubmit(form) {
    if(this.validateFormData(form)) {
      return;
    }

    let topics = [];

    for(let i = 0; i <= this.portalInfo.topics.length; i++) {
      let checked = form['topic_' + i];

      if(checked) {
        let topic = this.portalInfo.topics[i];
        topics.push(topic)
      }
    }

    this.assessmentService.createAssessment(form.name, form.instructions, topics, this.questions, this.portalId)
      .subscribe(result => {
        this.router.navigate(['/assessment-search', this.portalId])
      });
  }

  addQuestion(question) {
    let questionExists:boolean = false;

    for(let questizzle of this.questions) {
      if(questizzle.id == question.id) {
        questionExists = true;
        break;
      }
    }

    if(questionExists == false) {
      this.questions.push(question);
    }
  }

  removeQuestion(index) {
    this.questions.splice(index, 1);
  }

  getPortalInfo(id: string): void {
    this.portalService.getPortal(id)
      .subscribe(result => {
        this.portalInfo = result;

        let counter: number = 0;

        for(let topic of this.portalInfo.topics) {
          this.form.addControl('topic_' + counter, new FormControl());
          counter += 1;
        }
      });
  }

  getPortalId():string {
    return this.portalId;
  }
}
