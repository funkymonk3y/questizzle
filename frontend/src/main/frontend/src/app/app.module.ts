import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import { FullLayoutComponent } from "./full-layout/full-layout.component";
import { PrintAssessmentComponent } from "./assessment/print-assessment.component";

import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { ConfirmEmailSentComponent } from './register/confirm-email-sent.component';
import { ConfirmEmailConfirmedComponent } from './register/confirm-email-confirmed.component';
import { UserProfileComponent } from './user/user-profile.component';
import { AccessDeniedComponent } from './security/access-denied.component';

import { PortalSearchComponent } from './portal/portal-search.component';
import { CreatePortalComponent } from './portal/create-portal.component';
import { EditPortalComponent } from './portal/edit-portal.component';
import { PortalComponent } from './portal/portal.component';
import { CreateQuestionComponent } from './question/create-question.component';
import { EditQuestionComponent } from './question/edit-question.component';
import { QuestionComponent } from './question/question.component';

import { CreateSearchPopupComponent } from './question/create-search-popup.component';
import { EditSearchPopupComponent } from './question/edit-search-popup.component';

import { AssessmentComponent } from './assessment/assessment.component';
import { AssessmentSearchComponent } from './assessment/assessment-search.component';
import { CreateAssessmentComponent } from './assessment/create-assessment.component';
import { EditAssessmentComponent } from './assessment/edit-assessment.component';
import { PracticeTestComponent } from './assessment/practice-test.component';

import { AuthGuard } from './guards/auth.guard';
import { EqualValidator } from './directive/equal-validator.directive';

import { PortalService } from './portal/portal.service';
import { QuestionService } from './question/question.service';
import { AssessmentService } from './assessment/assessment.service';
import { LoginService } from './login/login.service';
import { RegisterService } from './register/register.service';
import { CommentService } from './question/comment.service';
import { UserService } from './user/user.service';

import { NewlinePipe } from './pipes/newlines.pipe';

import { routing } from './app.routing';

@NgModule({
  declarations: [
    PortalSearchComponent,
    CreatePortalComponent,
    EditPortalComponent,
    AppComponent,
    PortalComponent,
    CreateQuestionComponent,
    EditQuestionComponent,
    QuestionComponent,
    CreateSearchPopupComponent,
    EditSearchPopupComponent,
    AssessmentComponent,
    AssessmentSearchComponent,
    CreateAssessmentComponent,
    EditAssessmentComponent,
    PracticeTestComponent,
    LoginComponent,
    FullLayoutComponent,
    RegisterComponent,
    EqualValidator,
    ConfirmEmailSentComponent,
    ConfirmEmailConfirmedComponent,
    UserProfileComponent,
    PrintAssessmentComponent,
    AccessDeniedComponent,
    NewlinePipe
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpModule,
    routing
  ],
  providers: [
    PortalService,
    QuestionService,
    AssessmentService,
    AuthGuard,
    LoginService,
    RegisterService,
    CommentService,
    UserService
  ],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule { }
