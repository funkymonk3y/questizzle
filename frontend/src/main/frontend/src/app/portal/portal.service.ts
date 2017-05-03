import { Injectable } from '@angular/core';
import { Http, URLSearchParams, Response } from '@angular/http';
import 'rxjs/add/operator/map';
import { LoginService } from '../login/login.service';
import {Observable} from "rxjs";

@Injectable()
export class PortalService {

  constructor(
    private http: Http,
    private loginService: LoginService
  ) {}

  getPortal(id: string) {
    let headers = this.loginService.getAuthHeaders();

    return this.http.get('/api/portal/' + id, { headers: headers })
      .map(response => {
        return response.json();
      })
      .catch(e => {
        if(e.status === 403) {
          return Observable.throw(e.json().message);
        } else {
          return Observable.throw('Error getting portal.');
        }
      });
  }

  portalSearch(query: string) {
    let headers      = this.loginService.getAuthHeaders();
    let searchParams = new URLSearchParams();

    searchParams.append('query', query);

    return this.http.get('/api/portal/search', { search: searchParams, headers: headers })
      .map(response => {
        return response.json();
      });
  }

  getAllPortals() {
    let headers = this.loginService.getAuthHeaders();

    return this.http.get('/api/portal', { headers: headers })
      .map(response => {
        return response.json();
      });
  }

  createPortal(name: string, description: string, admins: Array<string>, users: Array<string>, topics: Array<string>) {
    let headers  = this.loginService.getAuthHeaders();
    let username = this.loginService.getUsername();

    let jsonPortal = JSON.stringify(
      {
        name: name,
        description: description,
        admins: admins,
        users: users,
        topics: topics,
        createdBy: { username }
      }
    );

    return this.http.post('/api/portal', jsonPortal, { headers: headers })
      .map((response: Response) => {
        return response.json();
      });
  }

  updatePortal(id: string, name: string, description: string, admins: Array<string>, users: Array<string>, topics: Array<string>) {
    let headers  = this.loginService.getAuthHeaders();
    let username = this.loginService.getUsername();

    let jsonPortal = JSON.stringify(
      {
        id: id,
        name: name,
        description: description,
        admins: admins,
        users: users,
        topics: topics,
        modifiedBy: { username }
      }
    );

    return this.http.put('/api/portal/' + id, jsonPortal, { headers: headers })
      .map((response: Response) => {
        return response.json();
      });
  }
}
