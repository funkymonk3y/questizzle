import { Injectable } from '@angular/core';
import { Http, URLSearchParams, Response } from '@angular/http';
import 'rxjs/add/operator/map';
import { LoginService } from '../login/login.service';
import { Observable } from 'rxjs';

@Injectable()
export class QuestionService {

  constructor(
    private http: Http,
    private loginService: LoginService
  ) {}

  getQuestion(portalId: string, questionId: string) {
    let headers = this.loginService.getAuthHeaders();
    let url:string = '/api/portal/' + portalId + '/question/' + questionId;

    return this.http.get(url, { headers: headers })
      .map(response => {
        return response.json();
      })
      .catch(e => {
        if(e.status === 403) {
          return Observable.throw(e.json().message);
        } else {
          return Observable.throw('Error getting question.');
        }
      });
  }

  getAllQuestions(portalId: string) {
    let headers = this.loginService.getAuthHeaders();

    return this.http.get('/api/portal/' + portalId + '/question', { headers: headers })
      .map(response => {
        return response.json();
      });
  }

  searchQuestions(portalId: string, query: string) {
    let headers = this.loginService.getAuthHeaders();
    let searchParams = new URLSearchParams();

    searchParams.append('query', query);

    return this.http.get('/api/portal/' + portalId + '/question/search', { search: searchParams, headers: headers })
      .map(response => {
        return response.json();
      });
  }

  createQuestion(portalId:string, question: string, answers: Array<any>, topics: Array<string>, hints: Array<string>) {
    let headers  = this.loginService.getAuthHeaders();
    let username = this.loginService.getUsername();

    let jsonQuestion = JSON.stringify(
      {
        text: question,
        answers: answers,
        topics: topics,
        hints: hints,
        createdBy: { username }
      }
    );

    let url:string = '/api/portal/' + portalId + '/question';

    return this.http.post(url, jsonQuestion, { headers: headers })
      .map((response: Response) => {
        return response.json();
      });
  }

  updateQuestion(question_id:string, portal_id:string, question: string, answers: Array<any>, topics: Array<string>, hints: Array<string>) {
    let headers  = this.loginService.getAuthHeaders();
    let username = this.loginService.getUsername();

    let jsonQuestion = JSON.stringify(
      {
        id: question_id,
        text: question,
        answers: answers,
        topics: topics,
        hints: hints,
        modifiedBy: { username }
      }
    );

    let url:string = '/api/portal/' + portal_id + '/question/' + question_id;

    return this.http.put(url, jsonQuestion, { headers: headers })
      .map((response: Response) => {
        return response.json();
      });
  }

  voteOnQuality(portalId:string, questionId:string, property:string) {
    let headers  = this.loginService.getAuthHeaders();
    let username = this.loginService.getUsername();

    let jsonData = JSON.stringify(
      {
        username: username,
        property: property
      }
    );

    let url:string = '/api/portal/' + portalId + '/question/' + questionId + "/vote/quality";

    return this.http.post(url, jsonData, { headers: headers })
      .map((response: Response) => {
        return response.json();
      });
  }

  voteOnDifficulty(portalId:string, questionId:string, property:string) {
    let headers  = this.loginService.getAuthHeaders();
    let username = this.loginService.getUsername();

    let jsonData = JSON.stringify(
      {
        username: username,
        property: property
      }
    );

    let url:string = '/api/portal/' + portalId + '/question/' + questionId + "/vote/difficulty";

    return this.http.post(url, jsonData, { headers: headers })
      .map((response: Response) => {
        return response.json();
      });
  }
}
