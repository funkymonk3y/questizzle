import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';
import { Observable } from 'rxjs';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';

@Injectable()
export class RegisterService {

  constructor(private http: Http) {}

  registerUser(username: string, email: string, password: string) {
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');

    let jsonPayload = JSON.stringify({ username: username, password: password, email: email });

    return this.http.post('/api/user/register', jsonPayload, { headers: headers })
      .map((response: Response) => {
        return response.json();
      })
      .catch(e => {
        if(e.status === 403) {
          return Observable.throw(e.json().message);
        } else {
          return Observable.throw('Error registering account.');
        }
      });
  }

  confirmUser(user_id:string, token:string) {
    return this.http.get('/api/user/confirm/' + user_id + '/' + token)
      .map((response: Response) => {
        return response;
      });
  }
}
