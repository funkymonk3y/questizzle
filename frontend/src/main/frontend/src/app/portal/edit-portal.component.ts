import { Component, OnInit, OnDestroy } from '@angular/core';
import { Validators, FormBuilder, FormGroup } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';

import { PortalService } from './portal.service';

@Component({
  selector: 'edit-portal',
  templateUrl: './edit-portal.component.html'
})
export class EditPortalComponent implements OnInit, OnDestroy {

  private form:FormGroup;

  private pathParamSub:any;

  private portal_id:string;
  private portal:any = null;

  constructor(
    private portalService: PortalService,
    private formBuilder: FormBuilder,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.pathParamSub = this.route.params.subscribe(params => {
      this.portal_id = params['id'];
      this.getPortalInfo(this.portal_id);
    });

    this.form = this.formBuilder.group({});
  }

  ngOnDestroy(): void {
    this.pathParamSub.unsubscribe();
  }

  getPortalInfo(id:string): void {
    this.portalService.getPortal(id)
      .subscribe(result => {
        this.portal = result;

        this.form.addControl('name', this.formBuilder.control(result.name, Validators.required));
        this.form.addControl('description', this.formBuilder.control(result.description, Validators.required));
        this.form.addControl('admins', this.formBuilder.control(result.admins.toString()));
        this.form.addControl('users', this.formBuilder.control(result.users.toString()));
        this.form.addControl('topics', this.formBuilder.control(result.topics.toString()));
      }, (err) => {
        this.router.navigate(["/access-denied"]);
      });
  }

  parseCommaDelimitedList(list) {
    let splitTrimmedValues = [];

    if(list !== null && list !== undefined) {
      if(list.length > 0) {
        let splitValues = list.split(',');

        for(let i = 0; i < splitValues.length; i++) {
          splitTrimmedValues.push(splitValues[i]);
        }
      }
    }

    return splitTrimmedValues;
  }

  onSubmit(portal) {
    let admins = this.parseCommaDelimitedList(portal.admins);
    let users  = this.parseCommaDelimitedList(portal.users);
    let topics = this.parseCommaDelimitedList(portal.topics);

    this.portalService.updatePortal(this.portal_id, portal.name, portal.description, admins, users, topics)
      .subscribe(result => {
        this.router.navigate(['/portal-search']);
      });
  }
}
