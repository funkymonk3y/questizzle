import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { RegisterService } from './register.service';

@Component({
  selector: 'confirm-email-confirmed',
  templateUrl: './confirm-email-confirmed.component.html'
})
export class ConfirmEmailConfirmedComponent implements OnInit, OnDestroy {

  private pathParamSub:any;

  constructor(
    private route: ActivatedRoute,
    private registerService: RegisterService
  ) {}

  ngOnInit(): void {
    this.pathParamSub = this.route.params.subscribe(params => {
      let user_id = params['user_id'];
      let token   = params['token'];

      this.registerService.confirmUser(user_id, token)
        .subscribe();
    });
  }

  ngOnDestroy(): void {
    this.pathParamSub.unsubscribe();
  }
}
