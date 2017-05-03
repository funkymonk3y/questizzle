import { Component, OnInit } from '@angular/core';
import { Validators, FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';

import { PortalService } from './portal.service';

@Component({
  selector: 'create-portal',
  templateUrl: './create-portal.component.html'
})
export class CreatePortalComponent implements OnInit {
  private form: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private portalService: PortalService,
    private router: Router
  ) {}

  ngOnInit() {
    this.form = this.formBuilder.group({
      name: this.formBuilder.control('', Validators.required),
      description: this.formBuilder.control('', Validators.required),
      admins: this.formBuilder.control(''),
      users: this.formBuilder.control(''),
      topics: this.formBuilder.control('', Validators.required)
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

    this.portalService.createPortal(portal.name, portal.description, admins, users, topics)
      .subscribe(result => {
        this.router.navigate(['/portal-search']);
    });
  }
}
