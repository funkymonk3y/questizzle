import { Component, OnInit, OnDestroy } from '@angular/core';
import { AssessmentService } from './assessment.service';
import {ActivatedRoute, Router} from '@angular/router';
import { CommentService } from '../question/comment.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { LoginService } from '../login/login.service';

@Component({
  selector: 'assessment',
  templateUrl: './assessment.component.html'
})
export class AssessmentComponent implements OnInit, OnDestroy {

  private commentForm:FormGroup
  private portal_id:string;
  private assessment_id:string;
  private assessment:any = null;
  private pathParamSub:any;
  private comments = [];
  private username:string;

  constructor(
    private formBuilder:FormBuilder,
    private assessmentService:AssessmentService,
    private commendService:CommentService,
    private route:ActivatedRoute,
    private router: Router,
    private loginService:LoginService
  ) {}

  ngOnInit(): void {
    this.username = this.loginService.getUsername();

    this.commentForm = this.formBuilder.group({
      comment: this.formBuilder.control('', Validators.required)
    });

    this.pathParamSub = this.route.params.subscribe(params => {
      this.portal_id     = params['portal_id'];
      this.assessment_id = params['id'];
      this.getAssessment(this.portal_id, this.assessment_id);
      this.getComments(this.portal_id, this.assessment_id);
    });
  }

  ngOnDestroy(): void {
    this.pathParamSub.unsubscribe();
  }

  getAssessment(portal_id:string, id:string) {
    this.assessmentService.getAssessment(portal_id, id)
      .subscribe(result => {
        this.assessment = result;
      },(err) => {
      this.router.navigate(["/access-denied"]);
    });
  }

  getComments(portal_id:string, id:string) {
    this.commendService.getAssessmentComments(portal_id, id)
      .subscribe(result => {
        this.comments = result;
      });
  }

  postComment(form) {
    this.commendService.submitAssessmentComment(form.comment, this.portal_id, this.assessment_id)
      .subscribe(result => {
        this.comments.unshift(result);
      });

    this.commentForm.reset();
  }

  onBadClick() {
    if(this.assessment.quality.voted != "bad") {
      this.assessmentService.voteOnQuality(this.portal_id, this.assessment_id, "bad")
        .subscribe(result => {
          this.assessment.quality = result;
        });
    }
  }

  onPoorClick() {
    if(this.assessment.quality.voted != "poor") {
      this.assessmentService.voteOnQuality(this.portal_id, this.assessment_id, "poor")
        .subscribe(result => {
          this.assessment.quality = result;
        });
    }
  }

  onAverageClick() {
    if(this.assessment.quality.voted != "average") {
      this.assessmentService.voteOnQuality(this.portal_id, this.assessment_id, "average")
        .subscribe(result => {
          this.assessment.quality = result;
        });
    }
  }

  onGoodClick() {
    if(this.assessment.quality.voted != "good") {
      this.assessmentService.voteOnQuality(this.portal_id, this.assessment_id, "good")
        .subscribe(result => {
          this.assessment.quality = result;
        });
    }
  }

  onExcellentClick() {
    if(this.assessment.quality.voted != "excellent") {
      this.assessmentService.voteOnQuality(this.portal_id, this.assessment_id, "excellent")
        .subscribe(result => {
          this.assessment.quality = result;
        });
    }
  }

  onEasyClick() {
    if(this.assessment.difficulty.voted != "easy") {
      this.assessmentService.voteOnDifficulty(this.portal_id, this.assessment_id, "easy")
        .subscribe(result => {
          this.assessment.difficulty = result;
        });
    }
  }

  onAverageDiffClick() {
    if(this.assessment.difficulty.voted != "average") {
      this.assessmentService.voteOnDifficulty(this.portal_id, this.assessment_id, "average")
        .subscribe(result => {
          this.assessment.difficulty = result;
        });
    }
  }

  onHardClick() {
    if(this.assessment.difficulty.voted != "hard") {
      this.assessmentService.voteOnDifficulty(this.portal_id, this.assessment_id, "hard")
        .subscribe(result => {
          this.assessment.difficulty = result;
        });
    }
  }

  printWindow() {
    let url:string = location.protocol + '//' + location.host + "/print-assessment/" + this.assessment_id;
    window.open(url, '_blank', 'location=no,height=600,width=800,scrollbars=yes,status=yes');
  }

  getMatchingLikeMetric(question_id:string) {
    let likeMetric = null;

    for(let i = 0; i < this.assessment.questionLikes.length; i++) {
      let like = this.assessment.questionLikes[i];

      if(like.identity == question_id) {
        likeMetric = like;
        break;
      }
    }

    return likeMetric;
  }

  getNumOfLikes(question_id:string) {
    let likeMetric = this.getMatchingLikeMetric(question_id);

    if(likeMetric != null) {
      return likeMetric.count;
    }
  }

  alreadyVoted(question_id:string) {
    let match:boolean = false;

    let likeMetric = this.getMatchingLikeMetric(question_id);

    if(likeMetric != null) {
      for(let j = 0; j < likeMetric.voted.length; j++) {
        if(likeMetric.voted[j] == this.username) {
          match = true;
          break;
        }
      }
    }

    return match;
  }

  likeQuestion(question_id:string) {
    if(this.alreadyVoted(question_id) == false) {
      this.assessmentService.voteOnQuestion(this.portal_id, this.assessment_id, question_id)
        .subscribe( result => {
          for(let i = 0; i < this.assessment.questionLikes.length; i++) {
            let like = this.assessment.questionLikes[i];

            if(like.identity == question_id) {
              this.assessment.questionLikes.splice(i, 0, result);
              break;
            }
          }
        });
    }
  }
}
