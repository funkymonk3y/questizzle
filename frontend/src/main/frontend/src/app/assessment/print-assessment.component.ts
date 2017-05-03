import { Component, OnInit, OnDestroy } from '@angular/core';
import { AssessmentService } from './assessment.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'print-assessment',
  templateUrl: './print-assessment.component.html'
})
export class PrintAssessmentComponent implements OnInit, OnDestroy {

  private portal_id:string;
  private assessment_id:string;

  private assessment:any = null;

  private pathParamSub:any;

  constructor(
    private assessmentService:AssessmentService,
    private route:ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.pathParamSub = this.route.params.subscribe(params => {
      this.portal_id     = params['portal_id'];
      this.assessment_id = params['id'];
      this.getAssessment(this.portal_id, this.assessment_id);
    });
  }

  ngOnDestroy(): void {
    this.pathParamSub.unsubscribe();
  }

  getAssessment(portal_id:string, id:string) {
    this.assessmentService.getAssessment(portal_id, id)
      .subscribe(result => {
        this.assessment = result;
      });
  }
}
