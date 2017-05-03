import { Injectable } from '@angular/core';
import { Http, URLSearchParams, Response } from '@angular/http';
import 'rxjs/add/operator/map';
import { LoginService } from '../login/login.service';
import { Observable } from 'rxjs';

@Injectable()
export class AssessmentService {

  constructor(
    private http: Http,
    private loginService: LoginService
  ) {}

  getAssessment(portal_id:string, assessment_id:string) {
    let headers = this.loginService.getAuthHeaders();

    let url:string = '/api/portal/' + portal_id + '/assessment/' + assessment_id;

    return this.http.get(url, { headers: headers })
      .map((response: Response) => {
        return response.json();
      }).catch(e => {
        if(e.status === 403) {
          return Observable.throw(e.json().message);
        } else {
          return Observable.throw('Error getting assessment.');
        }
      });
  }

  createAssessment(name: string, instructions: string, topics: Array<string>, questions: Array<any>, portal_id: string) {
    let headers  = this.loginService.getAuthHeaders();
    let username = this.loginService.getUsername();

    let jsonAssessment = JSON.stringify(
      {
        name: name,
        instructions: instructions,
        topics: topics,
        questions: questions,
        portal_id: portal_id,
        createdBy: {
          username: username
        }
      }
    );

    let url:string = '/api/portal/' + portal_id + '/assessment';

    return this.http.post(url, jsonAssessment, { headers: headers })
      .map((response: Response) => {
        return response.json();
      });
  }

  updateAssessment(name: string, instructions: string, topics: Array<string>,
                   questions: Array<any>, portal_id: string, assessment_id: string) {

    let headers  = this.loginService.getAuthHeaders();
    let username = this.loginService.getUsername();

    let jsonAssessment = JSON.stringify(
      {
        id: assessment_id,
        name: name,
        instructions: instructions,
        topics: topics,
        questions: questions,
        portal_id: portal_id,
        modifiedBy: {
          username: username
        }
      }
    );

    let url:string = '/api/portal/' + portal_id + '/assessment/' + assessment_id;

    return this.http.put(url, jsonAssessment, { headers: headers })
      .map((response: Response) => {
        return response.json();
      });
  }

  searchAssessments(portal_id: string, query:string) {
    let headers = this.loginService.getAuthHeaders();
    let searchParams = new URLSearchParams();

    searchParams.append('query', query);

    return this.http.get('/api/portal/' +  portal_id + '/assessment/search', { search: searchParams, headers: headers})
      .map(response => {
        return response.json();
      });
  }

  getAllAssessments(portal_id: string) {
    let headers = this.loginService.getAuthHeaders();

    return this.http.get('/api/portal/' + portal_id + '/assessment', { headers: headers })
      .map(response => {
        return response.json();
      });
  }

  voteOnQuality(portalId:string, assessmentId:string, property:string) {
    let headers  = this.loginService.getAuthHeaders();
    let username = this.loginService.getUsername();

    let jsonData = JSON.stringify(
      {
        username: username,
        property: property
      }
    );

    let url:string = '/api/portal/' + portalId + '/assessment/' + assessmentId + "/vote/quality";

    return this.http.post(url, jsonData, { headers: headers })
      .map((response: Response) => {
        return response.json();
      });
  }

  voteOnDifficulty(portalId:string, assessmentId:string, property:string) {
    let headers  = this.loginService.getAuthHeaders();
    let username = this.loginService.getUsername();

    let jsonData = JSON.stringify(
      {
        username: username,
        property: property
      }
    );

    let url:string = '/api/portal/' + portalId + '/assessment/' + assessmentId + "/vote/difficulty";

    return this.http.post(url, jsonData, { headers: headers })
      .map((response: Response) => {
        return response.json();
      });
  }

  voteOnQuestion(portalId:string, assessmentId:string, questionId:string) {
    let headers  = this.loginService.getAuthHeaders();
    let username = this.loginService.getUsername();

    let jsonData = JSON.stringify(
      {
        id: questionId,
        username: username
      }
    );

    let url:string = '/api/portal/' + portalId + '/assessment/' + assessmentId + '/vote/question';

    return this.http.post(url, jsonData, { headers: headers })
      .map((response: Response) => {
        return response.json();
      });
  }
}
