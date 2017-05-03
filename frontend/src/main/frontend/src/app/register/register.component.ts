import { Component, OnInit } from '@angular/core';
import { Validators, FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { RegisterService } from './register.service';

@Component({
  selector: 'register',
  templateUrl: './register.component.html'
})
export class RegisterComponent implements OnInit {
  private form: FormGroup;
  private errorMessage: string;
  private submitted:boolean = false;

  constructor(
    private formBuilder: FormBuilder,
    private registerService: RegisterService,
    private router: Router
  ) {}

  ngOnInit() {
    this.form = this.formBuilder.group({
      username: this.formBuilder.control('', Validators.required),
      email: this.formBuilder.control('', Validators.required),
      password: this.formBuilder.control('', Validators.required),
      confirmPassword: this.formBuilder.control('', Validators.required)
    });
  }

  onSubmit(reg) {
    this.submitted = true;

    this.registerService
      .registerUser(reg.username, reg.email, reg.password)
      .subscribe(result => {
        this.errorMessage = null;
        this.router.navigate(['/registration-confirmation']);
      }, (err) => {
        this.submitted = false;
        this.errorMessage = err;
    });
  }
}
