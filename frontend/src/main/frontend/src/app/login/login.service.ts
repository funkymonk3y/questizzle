import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';
import { Observable } from 'rxjs';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';

@Injectable()
export class LoginService {
  public token: string;
  public username: string;

  constructor(private http: Http) {
    var currentUser = JSON.parse(localStorage.getItem('currentUser'));
    this.token = currentUser && currentUser.token;
    this.username = currentUser && currentUser.username;
  }

  getAuthHeaders() {
    let headers = new Headers();
    let currentUser = JSON.parse(localStorage.getItem('currentUser'));

    headers.append('Content-Type', 'application/json');
    headers.append('Authorization', currentUser.token);

    return headers;
  }

  getUsername() {
    return this.username;
  }

  login(username: string, password: string): Observable<boolean> {
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');

    let jsonPayload = JSON.stringify({ username: username, password: password });

    return this.http.post('/api/auth', jsonPayload, { headers: headers })
      .map((response: Response) => {
        let token = response.json() && response.json().token;

        if(token) {
          this.token    = token;
          this.username = username;

          let jsonToken = JSON.stringify({ username: username, token: token });
          localStorage.setItem('currentUser', jsonToken);
          return true;
        } else {
          return false;
        }
      })
      .catch(e => {
        if(e.status === 401) {
          return Observable.throw('Unauthorized');
        }
      });
  }

  logout(): void {
    this.token = null;
    localStorage.removeItem('currentUser');
  }
}
