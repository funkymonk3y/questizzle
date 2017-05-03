import { Component, OnDestroy, OnInit } from '@angular/core';
import { Validators, FormBuilder, FormGroup } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';

import { PortalService } from './portal.service';

@Component({
  selector: 'portal-search',
  templateUrl: './portal-search.component.html'
})
export class PortalSearchComponent implements OnInit, OnDestroy {

  private form: FormGroup;
  private portals = [];
  private sub: any;

  constructor(
    private portalService: PortalService,
    private formBuilder: FormBuilder,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    let query = '';

    this.sub = this.route.queryParams.subscribe(queryParams => {
      query = queryParams['query'];

      if(query !== null && query !== undefined && query.length > 0) {
        this.performSearch(query);
      } else {
        this.getAllPortals();
      }
    });

    this.form = this.formBuilder.group({
      query: this.formBuilder.control(query, Validators.required)
    });
  }

  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }

  onFormSubmit(query): void {
    this.router.navigate(['/portal-search'], { queryParams: { query: query['query'] }});
  }

  performSearch(query) {
    this.portalService.portalSearch(query)
      .subscribe(searchResults => {
        this.portals = searchResults;
      });
  }

  getAllPortals() {
    this.portalService.getAllPortals()
      .subscribe(results => {
        this.portals = results;
      });
  }
}
