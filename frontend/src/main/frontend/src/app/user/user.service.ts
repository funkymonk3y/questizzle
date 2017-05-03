import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/add/operator/map';
import { LoginService } from '../login/login.service';

@Injectable()
export class UserService {

  constructor(
    private http: Http,
    private loginService: LoginService
  ) {}

  getUserInfo(username:string) {
    let headers = this.loginService.getAuthHeaders();
    let url:string = '/api/user/info/' + username;

    return this.http.get(url, { headers: headers })
      .map(response => {
        return response.json();
      });
  }
}
