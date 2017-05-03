import { Component, OnInit, OnDestroy } from '@angular/core';
import { Validators, FormBuilder, FormGroup } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';

import { AssessmentService } from './assessment.service';
import { PortalService } from '../portal/portal.service';

@Component({
  selector: 'assessment-search',
  templateUrl: './assessment-search.component.html'
})
export class AssessmentSearchComponent implements OnInit, OnDestroy {

  private form: FormGroup;
  private assessments = [];
  private pathParamSub: any;
  private queryParamSub: any;
  private portalId: string;
  private portalInfo: any = null;

  constructor(
    private portalService: PortalService,
    private assessmentService: AssessmentService,
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

      if(query !== null && query !== undefined) {
        this.performSearch(query);
      } else {
        this.getAllAssessments();
      }
    });

    this.form = this.formBuilder.group({
      query: this.formBuilder.control(query, Validators.required)
    });
  }

  ngOnDestroy(): void {
    this.queryParamSub.unsubscribe();
  }

  onFormSubmit(query): void {
    this.router.navigate(['/assessment-search', this.portalId], { queryParams: { query: query['query'] }});
  }

  performSearch(query) {
    this.assessmentService.searchAssessments(this.portalId, query)
      .subscribe(searchResults => {
        this.assessments = searchResults;
      }
    );
  }

  getPortalInfo(id: string): void {
    this.portalService.getPortal(id)
      .subscribe(result => {
        this.portalInfo = result;
      });
  }

  getAllAssessments() {
    this.assessmentService.getAllAssessments(this.portalId)
      .subscribe(results => {
        this.assessments = results;
      });
  }

  private amtOfPositiveQualityVotes(assessment):number {
    let positiveCount:number = 0;

    positiveCount += assessment.quality.votes.average;
    positiveCount += assessment.quality.votes.good;
    positiveCount += assessment.quality.votes.excellent;

    return positiveCount;
  }

  private amtOfQualityVotes(assessment):number {
    let totalCount:number = 0;

    totalCount += assessment.quality.votes.bad;
    totalCount += assessment.quality.votes.poor;
    totalCount += assessment.quality.votes.average;
    totalCount += assessment.quality.votes.good;
    totalCount += assessment.quality.votes.excellent;

    return totalCount;
  }

  private qualityPercentage(assessment):string {
    let positiveCount = this.amtOfPositiveQualityVotes(assessment);
    let totalCount    = this.amtOfQualityVotes(assessment);

    let percentage = 0;

    if(totalCount > 0) {
      percentage = (positiveCount / totalCount) * 100;
    }

    return percentage.toFixed(2);
  }

  private amtOfDifficultyVotes(assessment):number {
    let totalCount:number = 0;

    totalCount += assessment.difficulty.votes.easy;
    totalCount += assessment.difficulty.votes.average;
    totalCount += assessment.difficulty.votes.hard;

    return totalCount;
  }

  private difficultyPercentage(assessment):string {
    let difficultyCount = assessment.difficulty.votes.hard;
    let totalCount      = this.amtOfDifficultyVotes(assessment);

    let percentage = 0;

    if(totalCount > 0) {
      percentage = (difficultyCount / totalCount) * 100;
    }

    return percentage.toFixed(2);
  }
}
