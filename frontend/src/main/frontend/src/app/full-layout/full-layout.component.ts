import { Component, OnInit } from '@angular/core';
import { LoginService } from "../login/login.service";
import { Router } from "@angular/router";

@Component({
  selector: 'full-layout',
  templateUrl: './full-layout.component.html'
})
export class FullLayoutComponent implements OnInit {

  private username: string;

  constructor(
    private loginService: LoginService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.username = this.loginService.getUsername();
  }

  logout() {
    console.log("Logging out...");
    this.loginService.logout();
    this.router.navigate(['/']);
  }
}
