import { Component, OnDestroy, OnInit } from '@angular/core';
import { Validators, FormBuilder, FormGroup } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';

import { PortalService } from './portal.service';
import { QuestionService } from '../question/question.service';

@Component({
  selector: 'portal',
  templateUrl: './portal.component.html'
})
export class PortalComponent implements OnInit, OnDestroy {

  private form: FormGroup;
  private questions = [];
  private queryParamSub: any;
  private pathParamSub: any;
  private portalId: string;
  private portalInfo: any = null;

  constructor(
    private portalService: PortalService,
    private questionService: QuestionService,
    private formBuilder: FormBuilder,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.pathParamSub = this.route.params.subscribe(params => {
      this.portalId = params['id'];
      this.getPortalInfo(this.portalId);
    });

    let query = '';

    this.queryParamSub = this.route.queryParams.subscribe(queryParams => {
      query = queryParams['query'];

      if(query !== null && query !== undefined && query.length > 0) {
        this.performSearch(query);
      } else {
        this.getAllQuestions();
      }
    });

    this.form = this.formBuilder.group({
      query: this.formBuilder.control(query, Validators.required)
    });
  }

  ngOnDestroy(): void {
    this.queryParamSub.unsubscribe();
    this.pathParamSub.unsubscribe();
  }

  onFormSubmit(query): void {
    this.router.navigate(['/portal', this.portalId], { queryParams: { query: query['query'] }})
  }

  performSearch(query) {
    this.questionService.searchQuestions(this.portalId, query)
      .subscribe(searchResults => {
        this.questions = searchResults;
      });
  }

  getPortalInfo(id: string): void {
    this.portalService.getPortal(id)
      .subscribe(result => {
        this.portalInfo = result;
      }, (err) => {
        this.router.navigate(["/access-denied"]);
      });
  }

  getAllQuestions() {
    this.questionService.getAllQuestions(this.portalId)
      .subscribe(results => {
        this.questions = results;
      });
  }

  private amtOfPositiveQualityVotes(question):number {
    let positiveCount:number = 0;

    positiveCount += question.quality.votes.average;
    positiveCount += question.quality.votes.good;
    positiveCount += question.quality.votes.excellent;

    return positiveCount;
  }

  private amtOfQualityVotes(question):number {
    let totalCount:number = 0;

    totalCount += question.quality.votes.bad;
    totalCount += question.quality.votes.poor;
    totalCount += question.quality.votes.average;
    totalCount += question.quality.votes.good;
    totalCount += question.quality.votes.excellent;

    return totalCount;
  }

  private qualityPercentage(question):string {
    let positiveCount = this.amtOfPositiveQualityVotes(question);
    let totalCount    = this.amtOfQualityVotes(question);

    let percentage = 0;

    if(totalCount > 0) {
      percentage = (positiveCount / totalCount) * 100;
    }

    return percentage.toFixed(2);
  }

  private amtOfDifficultyVotes(question):number {
    let totalCount:number = 0;

    totalCount += question.difficulty.votes.easy;
    totalCount += question.difficulty.votes.average;
    totalCount += question.difficulty.votes.hard;

    return totalCount;
  }

  private difficultyPercentage(question):string {
    let difficultyCount = question.difficulty.votes.hard;
    let totalCount      = this.amtOfDifficultyVotes(question);

    let percentage = 0;

    if(totalCount > 0) {
      percentage = (difficultyCount / totalCount) * 100;
    }

    return percentage.toFixed(2);
  }
}
