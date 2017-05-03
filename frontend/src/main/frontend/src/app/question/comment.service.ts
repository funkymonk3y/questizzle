import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import 'rxjs/add/operator/map';
import { LoginService } from '../login/login.service';

@Injectable()
export class CommentService {

  constructor(
    private http: Http,
    private loginService: LoginService
  ) {}

  submitAssessmentComment(comment:string, portalId:string, assessmentId:string) {
    let headers  = this.loginService.getAuthHeaders();
    let username = this.loginService.getUsername();

    let jsonComment = JSON.stringify(
      {
        text: comment,
        author: {
          username: username
        }
      }
    );

    let url:string = '/api/portal/' + portalId + '/assessment/' + assessmentId + '/comment';

    return this.http.post(url, jsonComment, { headers: headers })
      .map((response: Response) => {
        return response.json();
      });
  }

  submitQuestionComment(comment:string, portalId:string, questionId:string) {
    let headers  = this.loginService.getAuthHeaders();
    let username = this.loginService.getUsername();

    let jsonComment = JSON.stringify(
      {
        text: comment,
        author: {
          username: username
        }
      }
    );

    let url:string = '/api/portal/' + portalId + '/question/' + questionId + '/comment';

    return this.http.post(url, jsonComment, { headers: headers })
      .map((response: Response) => {
        return response.json();
      });
  }

  getQuestionComments(portalId:string, questionId:string) {
    let headers = this.loginService.getAuthHeaders();

    let url:string = '/api/portal/' + portalId + '/question/' + questionId + '/comment';

    return this.http.get(url, { headers: headers })
      .map((response: Response) => {
        return response.json();
      });
  }

  getAssessmentComments(portalId:string, assessmentId:string) {
    let headers = this.loginService.getAuthHeaders();

    let url:string = '/api/portal/' + portalId + '/assessment/' + assessmentId + '/comment';

    return this.http.get(url, { headers: headers })
      .map((response: Response) => {
        return response.json();
      });
  }
}
