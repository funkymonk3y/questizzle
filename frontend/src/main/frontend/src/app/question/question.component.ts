import { Component, OnDestroy, OnInit } from '@angular/core';
import { Validators, FormBuilder, FormGroup, FormControl } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

import { QuestionService } from '../question/question.service';
import { CommentService } from './comment.service';

@Component({
  selector: 'question',
  templateUrl: './question.component.html'
})
export class QuestionComponent implements OnInit, OnDestroy {

  private form: FormGroup;
  private commentForm: FormGroup;

  private portalId: string;
  private questionId: string;
  private pathParamSub: any;
  private question: any = null;

  private choicesTab: boolean = true;
  private hintsTab: boolean = false;
  private feedbackTab: boolean = false;

  private revealedHints = [];
  private comments = [];

  private answerIncorrectly: boolean;

  constructor(
    private formBuilder: FormBuilder,
    private questionService: QuestionService,
    private commentService: CommentService,
    private route: ActivatedRoute,
    private router: Router,
    private location: Location
  ) {}

  ngOnInit(): void {
    this.form = this.formBuilder.group({});
    this.commentForm = this.formBuilder.group({
      comment: this.formBuilder.control('', Validators.required)
    });

    this.pathParamSub = this.route.params.subscribe(params => {
      this.portalId   = params['portalId'];
      this.questionId = params['id'];
      this.getQuestion(this.portalId, this.questionId);
      this.getAllComments(this.portalId, this.questionId);
    });
  }

  ngOnDestroy(): void {
    this.pathParamSub.unsubscribe();
  }

  getQuestion(portalId: string, id: string): void {
    this.questionService.getQuestion(portalId, id)
      .subscribe(result => {
        // result.text = result.text.replace(/(?:\r\n|\r|\n)/g, '<br />');
        this.question = result;

        for(let i = 1; i <= this.question.answers.length; i++) {
          this.form.addControl('answer_' + i, new FormControl());
        }
      }, (err) => {
        this.router.navigate(["/access-denied"]);
      });
  }

  checkAnswers(form) {
    this.answerIncorrectly = false;

    let i: number = 1;

    for(let answer of this.question.answers) {
      let userAnswer = form['answer_' + i];

      if(userAnswer == null || userAnswer == undefined) {
        userAnswer = false;
      }

      if(userAnswer != answer.correct) {
        this.answerIncorrectly = true
      }

      i += 1;
    }
  }

  postComment(form) {
    this.commentService.submitQuestionComment(form.comment, this.portalId, this.questionId)
      .subscribe(result => {
        this.comments.unshift(result);
      });

    this.commentForm.reset();
  }

  getAllComments(portalId:string, questionId:string) {
    this.commentService.getQuestionComments(portalId, questionId)
      .subscribe(result => {
        this.comments = result;
      });
  }

  onBackClick(): void {
    this.location.back();
  }

  onChoicesTabClick(): void {
    this.choicesTab = true;
    this.hintsTab = false;
    this.feedbackTab = false;
  }

  onHintsTabClick(): void {
    this.choicesTab = false;
    this.hintsTab = true;
    this.feedbackTab = false;
  }

  onFeedbackTabClick(): void {
    this.choicesTab = false;
    this.hintsTab = false;
    this.feedbackTab = true;
  }

  revealHint() {
    let latestCount = this.revealedHints.length;

    if(latestCount < this.question.hints.length) {
      this.revealedHints.push(this.question.hints[latestCount]);
    }
  }

  onBadClick() {
    if(this.question.quality.voted != "bad") {
      this.questionService.voteOnQuality(this.portalId, this.questionId, "bad")
        .subscribe(result => {
          this.question.quality = result;
        });
    }
  }

  onPoorClick() {
    if(this.question.quality.voted != "poor") {
      this.questionService.voteOnQuality(this.portalId, this.questionId, "poor")
        .subscribe(result => {
          this.question.quality = result;
        });
    }
  }

  onAverageClick() {
    if(this.question.quality.voted != "average") {
      this.questionService.voteOnQuality(this.portalId, this.questionId, "average")
        .subscribe(result => {
          this.question.quality = result;
        });
    }
  }

  onGoodClick() {
    if(this.question.quality.voted != "good") {
      this.questionService.voteOnQuality(this.portalId, this.questionId, "good")
        .subscribe(result => {
          this.question.quality = result;
        });
    }
  }

  onExcellentClick() {
    if(this.question.quality.voted != "excellent") {
      this.questionService.voteOnQuality(this.portalId, this.questionId, "excellent")
        .subscribe(result => {
          this.question.quality = result;
        });
    }
  }

  onEasyClick() {
    if(this.question.difficulty.voted != "easy") {
      this.questionService.voteOnDifficulty(this.portalId, this.questionId, "easy")
        .subscribe(result => {
          this.question.difficulty = result;
        });
    }
  }

  onAverageDiffClick() {
    if(this.question.difficulty.voted != "average") {
      this.questionService.voteOnDifficulty(this.portalId, this.questionId, "average")
        .subscribe(result => {
          this.question.difficulty = result;
        });
    }
  }

  onHardClick() {
    if(this.question.difficulty.voted != "hard") {
      this.questionService.voteOnDifficulty(this.portalId, this.questionId, "hard")
        .subscribe(result => {
          this.question.difficulty = result;
        });
    }
  }
}
