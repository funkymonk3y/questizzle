import { Component, OnInit, OnDestroy } from '@angular/core';
import { Validators, FormBuilder, FormGroup, FormArray, FormControl } from '@angular/forms';

import { AssessmentService } from './assessment.service';
import { ActivatedRoute, Router } from '@angular/router';
import {PortalService} from "../portal/portal.service";

@Component({
  selector: 'edit-assessment',
  templateUrl: './edit-assessment.component.html'
})
export class EditAssessmentComponent implements OnInit, OnDestroy {

  private form:FormGroup;

  private portal_id:string;
  private assessment_id:string;

  private portal:any = null;
  private assessment:any = null;

  private pathParamSub:any;

  private topicIndexes = [];

  private topicNotChosenError:boolean = false;

  constructor(
    private formBuilder: FormBuilder,
    private portalService: PortalService,
    private assessmentService: AssessmentService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.form = this.formBuilder.group({});

    this.pathParamSub = this.route.params.subscribe(params => {
      this.portal_id     = params['portal_id'];
      this.assessment_id = params['id'];

      this.getPortal(this.portal_id);
    });
  }

  ngOnDestroy(): void {
    this.pathParamSub.unsubscribe();
  }

  getAssessment(portal_id:string, id:string) {
    this.assessmentService.getAssessment(portal_id, id)
      .subscribe(result => {
        this.assessment = result;

        this.form.addControl('name', this.formBuilder.control(result.name, Validators.required));
        this.form.addControl('instructions', this.formBuilder.control(result.instructions, Validators.required));

        for(let i = 1; i <= this.portal.topics.length; i++) {
          this.topicIndexes.push(i);

          let match = false;

          for(let topic of result.topics) {
            if(this.portal.topics[i - 1] == topic) {
              match = true;
            }
          }

          this.form.addControl('topic_' + i,  this.formBuilder.control(match));
        }
      });
  }

  getPortal(id: string): void {
    this.portalService.getPortal(id)
      .subscribe(result => {
        this.portal = result;

        this.getAssessment(this.portal_id, this.assessment_id);
      },(err) => {
        this.router.navigate(["/access-denied"]);
      });
  }

  removeQuestion(index) {
    this.assessment.questions.splice(index, 1);
  }

  getPortalId():string {
    return this.portal_id;
  }

  addQuestion(question) {
    let questionExists:boolean = false;

    for(let questizzle of this.assessment.questions) {
      if(questizzle.id == question.id) {
        questionExists = true;
        break;
      }
    }

    if(questionExists == false) {
      this.assessment.questions.push(question);
    }
  }

  validateFormData(form) {
    let result:boolean = false;

    let atLeastOneTopicChosen:boolean = false;

    for(let i = 0; i < this.portal.topics.length; i++) {
      let checked = form['topic_' + (i + 1)];

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

    for(let i = 0; i < this.portal.topics.length; i++) {
      let checked = form['topic_' + (i + 1)];

      if(checked) {
        let topic = this.portal.topics[i];
        topics.push(topic)
      }
    }

    this.assessmentService.updateAssessment(form.name, form.instructions, topics,
                                            this.assessment.questions, this.portal_id, this.assessment_id, )
      .subscribe(result => {
        this.router.navigate(['/assessment-search', this.portal_id])
      });
  }
}
