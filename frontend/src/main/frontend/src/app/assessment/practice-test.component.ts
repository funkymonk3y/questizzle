import { Component, OnInit, OnDestroy } from '@angular/core';
import { PortalService } from '../portal/portal.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormGroup, FormBuilder, FormControl } from '@angular/forms';
import { QuestionService } from '../question/question.service';

@Component({
  selector: 'practice-test',
  templateUrl: './practice-test.component.html'
})
export class PracticeTestComponent implements OnInit, OnDestroy {

  private formAnswers:FormGroup;

  private portal_id:string;

  private pathParamSub:any;
  private portalInfo:any = null;

  private questions = [];
  private revealedHints = [];

  private q_index:number = 0;

  private choicesTab:boolean = true;
  private hintsTab:boolean = false;

  private correctQuestions:number = 0;
  private incorrectQuestions:number = 0;
  private remainingQuestions:number = 0;

  private percentCorrect:string = "0";
  private percentIncorrect:string = "0";
  private percentRemaining:string = "100";

  constructor(
    private formBuilder:FormBuilder,
    private portalService:PortalService,
    private questionService:QuestionService,
    private route:ActivatedRoute,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.formAnswers = this.formBuilder.group({});

    this.pathParamSub = this.route.params.subscribe(params => {
      this.portal_id = params['id'];
      this.getPortalInfo(this.portal_id);
      this.getQuestions(this.portal_id);
    });
  }

  ngOnDestroy(): void {
    this.pathParamSub.unsubscribe();
  }

  getPortalInfo(id:string): void {
    this.portalService.getPortal(id)
      .subscribe(result => {
        this.portalInfo = result;
      }, (err) => {
        this.router.navigate(["/access-denied"]);
      });
  }

  getQuestions(id:string) {
    this.questionService.getAllQuestions(id)
      .subscribe(results => {
        this.questions = this.shuffleArray(results);
        this.setupCheckboxes(this.questions, this.q_index);
      });
  }

  resetForm(questions, q_index) {
    for(let i = 1; i <= questions[q_index].answers.length; i++) {
      this.formAnswers.removeControl('answer_' + i);
    }

    this.revealedHints = [];
    this.formAnswers.reset();
  }

  setupCheckboxes(questions, q_index) {
    for(let i = 1; i <= questions[q_index].answers.length; i++) {
      this.formAnswers.addControl('answer_' + i, new FormControl());
    }
  }

  checkAnswers(form) {
    let i: number = 1;

    let answerIncorrectly:boolean = false;

    for(let answer of this.questions[this.q_index].answers) {
      let userAnswer = form['answer_' + i];

      if(userAnswer == null || userAnswer == undefined) {
        userAnswer = false;
        this.questions[this.q_index].answers[i - 1].answerChecked = false;
      } else {
        this.questions[this.q_index].answers[i - 1].answerChecked = true;
      }

      if(userAnswer != answer.correct) {
        answerIncorrectly = true
      }

      i += 1;
    }

    if(answerIncorrectly) {
      this.incorrectQuestions += 1;
    } else {
      this.correctQuestions += 1;
    }

    this.questions[this.q_index].answered = true;
    this.questions[this.q_index].answerIncorrectly = answerIncorrectly;

    this.calculatePercentages();
  }

  calculatePercentages() {
    let numOfQuestions = this.questions.length;
    this.remainingQuestions = numOfQuestions - (this.correctQuestions + this.incorrectQuestions)

    let percentCorrect = (this.correctQuestions / numOfQuestions) * 100;
    let percentIncorrect = (this.incorrectQuestions / numOfQuestions) * 100;
    let percentRemaining = (this.remainingQuestions / numOfQuestions) * 100;

    this.percentCorrect = percentCorrect.toFixed(2);
    this.percentIncorrect = percentIncorrect.toFixed(2);
    this.percentRemaining = percentRemaining.toFixed(2);
  }

  onChoicesTabClick(): void {
    this.choicesTab = true;
    this.hintsTab = false;
  }

  onHintsTabClick(): void {
    this.choicesTab = false;
    this.hintsTab = true;
  }

  onFeedbackTabClick(): void {
    this.choicesTab = false;
    this.hintsTab = false;
  }

  revealHint() {
    let latestCount = this.revealedHints.length;

    if(latestCount < this.questions[this.q_index].hints.length) {
      this.revealedHints.push(this.questions[this.q_index].hints[latestCount]);
    }
  }

  previousClick() {
    if(this.q_index > 0) {
      this.resetForm(this.questions, this.q_index);
      this.q_index -= 1;
      this.setupCheckboxes(this.questions, this.q_index);
    }
  }

  nextClick() {
    if((this.questions.length - 1) > this.q_index) {
      this.resetForm(this.questions, this.q_index);
      this.q_index += 1;
      this.setupCheckboxes(this.questions, this.q_index);
    }
  }

  shuffleArray(array: Array<any>): Array<any> {
    for(let i = array.length - 1; i > 0; i--) {
      let j = Math.floor(Math.random() * (i + 1));
      let temp = array[i];
      array[i] = array[j];
      array[j] = temp;
    }

    return array;
  }
}
