import { Component, OnDestroy, OnInit } from '@angular/core';
import { Validators, FormBuilder, FormGroup, FormControl } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { PortalService } from '../portal/portal.service';
import { QuestionService } from './question.service';

@Component({
  selector: 'edit-question',
  templateUrl: './edit-question.component.html'
})
export class EditQuestionComponent implements OnInit, OnDestroy {

  private form:FormGroup;

  private portal_id:string;
  private question_id:string;

  private pathParamSub:any;

  private question:any = null;
  private portal:any = null;

  private answerIndexes = [];
  private topicIndexes = [];
  private hintIndexes = [];

  private errorMessages = [];

  constructor(
    private formBuilder: FormBuilder,
    private questionService: QuestionService,
    private portalService: PortalService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.form = this.formBuilder.group({});

    this.pathParamSub = this.route.params.subscribe(params => {
      this.portal_id   = params['portalId'];
      this.question_id = params['id'];
      this.getPortal(this.portal_id);
    });
  }

  ngOnDestroy(): void {
    this.pathParamSub.unsubscribe();
  }

  getQuestion(portal_id:string, question_id:string):void {
    this.questionService.getQuestion(portal_id, question_id)
      .subscribe(result => {
        this.question = result;

        this.form.addControl('question', this.formBuilder.control(result.text, Validators.required));

        for(let i = 1; i <= result.answers.length; i++) {
          this.answerIndexes.push(i);
          this.form.addControl('answer_' + i, this.formBuilder.control(result.answers[i - 1].text, Validators.required));
          this.form.addControl('answer_correct_' + i, this.formBuilder.control(result.answers[i - 1].correct));
        }

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

        for(let i = 1; i <= result.hints.length; i++) {
          this.hintIndexes.push(i);
          this.form.addControl('hint_' + i, this.formBuilder.control(result.hints[i - 1], Validators.required));
        }
      });
  }

  getPortal(portal_id:string): void {
    this.portalService.getPortal(portal_id)
      .subscribe(result => {
        this.portal = result;
        this.getQuestion(this.portal_id, this.question_id);
      }, (err) => {
        this.router.navigate(["/access-denied"]);
      });
  }

  removeAnswer() {
    let latestIndex = this.answerIndexes.length;

    this.form.removeControl('answer_' + latestIndex);
    this.form.removeControl('answer_correct_' + latestIndex);

    this.answerIndexes.pop();
  }

  addAnswer() {
    let counter = this.answerIndexes.length + 1;
    this.answerIndexes.push(counter);

    this.form.addControl('answer_' + counter, new FormControl('', Validators.required));
    this.form.addControl('answer_correct_' + counter, new FormControl());
  }

  removeHint() {
    let latestIndex = this.hintIndexes.length;

    this.form.removeControl('hint_' + latestIndex);

    this.hintIndexes.pop();
  }

  addHint() {
    let counter = this.hintIndexes.length + 1;
    this.hintIndexes.push(counter);

    this.form.addControl('hint_' + counter, new FormControl('', Validators.required));
  }

  validation(form): boolean {
    this.errorMessages = [];

    let result:boolean = false;

    let atLeastOneAnswerIsCorrect:boolean = false;

    for(let i = 1; i <= this.answerIndexes.length; i++) {
      let correct = form["answer_correct_" + i];

      if(correct) {
        atLeastOneAnswerIsCorrect = true;
      }
    }

    if(atLeastOneAnswerIsCorrect == false) {
      this.errorMessages.push("At least one answer must be marked as correct.");
      result = true;
    }

    let atLeastOneTopicChosen:boolean = false;

    for(let i = 1; i <= this.portal.topics.length; i++) {
      let checked = form["topic_" + i];

      if(checked) {
        atLeastOneTopicChosen = true;
      }
    }

    if(atLeastOneTopicChosen == false) {
      this.errorMessages.push("At least one topic needs to be chosen.");
      result = true;
    }

    return result;
  }

  onFormSubmit(form): void {
    if(this.validation(form)) {
      return;
    }

    let numOfAnswers:number = this.answerIndexes.length;
    let numOfTopics:number  = this.portal.topics.length;
    let numOfHints:number   = this.hintIndexes.length;

    let answers = [];

    for(let i = 1; i <= numOfAnswers; i++) {
      let answer  = form["answer_" + i];
      let correct = form["answer_correct_" + i];

      if(correct == null) {
        correct = false;
      }

      answers.push({ text: answer, correct: correct });
    }

    let topics = [];

    for(let i = 0; i < numOfTopics; i++) {
      let checked = form["topic_" + (i + 1)];

      if(checked) {
        let topic = this.portal.topics[i];
        topics.push(topic);
      }
    }

    let hints = [];

    for(let i = 1; i <= numOfHints; i++) {
      let hint = form["hint_" + i];
      hints.push(hint);
    }

    this.questionService.updateQuestion(this.question_id, this.portal_id, form.question, answers, topics, hints)
      .subscribe(result => {
        this.router.navigate(['/portal', this.portal_id]);
      });
  }
}
