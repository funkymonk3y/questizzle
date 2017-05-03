import { Component, OnInit } from '@angular/core';
import { Validators, FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';

import { LoginService } from './login.service';

@Component({
  selector: 'login',
  templateUrl: './login.component.html'
})
export class LoginComponent implements OnInit {
  private form: FormGroup;
  private loginError: boolean;

  constructor(
    private formBuilder: FormBuilder,
    private loginService: LoginService,
    private router: Router,
  ) {}

  ngOnInit() {
    this.loginService.logout();

    this.form = this.formBuilder.group({
      username: this.formBuilder.control('', Validators.required),
      password: this.formBuilder.control('', Validators.required)
    });
  }

  onSubmit(loginInfo) {
    this.loginService.login(loginInfo.username, loginInfo.password)
      .subscribe(result => {
        if(result === true) {
          this.loginError = false;
          this.router.navigate(['/']);
        } else {
          this.loginError = true;
        }
    }, (err) => {
      if(err === 'Unauthorized') {
        this.loginError = true;
      }
    });
  }
}
