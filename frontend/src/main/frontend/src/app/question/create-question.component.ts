import { Component, OnDestroy, OnInit } from '@angular/core';
import { Validators, FormBuilder, FormGroup, FormControl } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { PortalService } from '../portal/portal.service';
import { QuestionService } from './question.service';

@Component({
  selector: 'create-question',
  templateUrl: './create-question.component.html'
})
export class CreateQuestionComponent implements OnInit, OnDestroy {

  private form: FormGroup;
  private portalId: string;
  private portalInfo: any = null;
  private sub: any;
  private answerIndexes = [];
  private hintIndexes = [];

  private questionPill: boolean = true;
  private answersPill: boolean = false;
  private categoriesPill: boolean = false;
  private hintsPill: boolean = false;

  private errorMessages = [];

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private route: ActivatedRoute,
    private portalService: PortalService,
    private questionService: QuestionService
  ) {}

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      question: this.formBuilder.control('', Validators.required)
    });

    this.addAnswer();
    this.addHint();

    this.sub = this.route.params.subscribe(params => {
      this.portalId = params['id'];

      if(this.portalId === null || this.portalId === undefined) {
        this.router.navigate(['/portal-search']);
      } else {
        this.getPortalInfo(this.portalId);
      }
    });
  }

  ngOnDestroy(): void {
    this.sub.unsubscribe();
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

    for(let i = 1; i <= this.portalInfo.topics.length; i++) {
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
    let numOfTopics:number  = this.portalInfo.topics.length;
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
      let checked = form["topic_" + i];

      if(checked) {
        let topic = this.portalInfo.topics[i];
        topics.push(topic);
      }
    }

    let hints = [];

    for(let i = 1; i <= numOfHints; i++) {
      let hint = form["hint_" + i];
      hints.push(hint);
    }

    this.questionService.createQuestion(this.portalId, form.question, answers, topics, hints)
      .subscribe(result => {
        this.router.navigate(['/portal', this.portalId]);
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

  questionPillClick() {
    this.questionPill = true;
    this.answersPill = false;
    this.categoriesPill = false;
    this.hintsPill = false;
  }

  answersPillClick() {
    this.questionPill = false;
    this.answersPill = true;
    this.categoriesPill = false;
    this.hintsPill = false;
  }

  categoriesPillClick() {
    this.questionPill = false;
    this.answersPill = false;
    this.categoriesPill = true;
    this.hintsPill = false;
  }

  hintsPillClick() {
    this.questionPill = false;
    this.answersPill = false;
    this.categoriesPill = false;
    this.hintsPill = true;
  }

  previousClick() {
    if(this.answersPill) {
      this.questionPill = true;
      this.answersPill = false;
    }
    else if(this.categoriesPill) {
      this.answersPill = true;
      this.categoriesPill = false;
    }
    else if(this.hintsPill) {
      this.categoriesPill = true;
      this.hintsPill = false;
    }
  }

  nextClick() {
    if(this.questionPill) {
      this.questionPill = false;
      this.answersPill = true;
    }
    else if(this.answersPill) {
      this.answersPill = false;
      this.categoriesPill = true;
    }
    else if(this.categoriesPill) {
      this.categoriesPill = false;
      this.hintsPill = true;
    }
  }

  onBackClick() {
    this.router.navigate(['/portal', this.portalId]);
  }
}
