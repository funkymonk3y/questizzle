import { Component, OnDestroy, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import { UserService } from './user.service';

@Component({
  selector: 'user-profile',
  templateUrl: './user-profile.component.html'
})
export class UserProfileComponent implements OnInit, OnDestroy {

  private pathParamSub:any;

  private user:any = null;

  private username:string;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService,
    private location: Location
  ) {}

  ngOnInit(): void {
    this.pathParamSub = this.route.params.subscribe(params => {
      this.username = params['username'];
      this.getUserInfo(this.username);
    });
  }

  ngOnDestroy(): void {
    this.pathParamSub.unsubscribe();
  }

  getUserInfo(username:string) {
    this.userService.getUserInfo(username)
      .subscribe(result => {
        this.user = result;
      });
  }

  goBack() {
    this.location.back();
  }
}
