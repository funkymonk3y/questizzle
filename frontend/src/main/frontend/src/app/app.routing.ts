import { Routes, RouterModule } from '@angular/router';

import { AuthGuard } from './guards/auth.guard';
import { LoginComponent } from './login/login.component';
import { FullLayoutComponent } from './full-layout/full-layout.component';
import { RegisterComponent } from './register/register.component';
import { ConfirmEmailSentComponent } from './register/confirm-email-sent.component';
import { ConfirmEmailConfirmedComponent } from './register/confirm-email-confirmed.component';
import { AccessDeniedComponent } from './security/access-denied.component';

import { PortalSearchComponent }  from './portal/portal-search.component';
import { CreatePortalComponent } from './portal/create-portal.component';
import { EditPortalComponent } from './portal/edit-portal.component';
import { PortalComponent } from './portal/portal.component';

import { AssessmentSearchComponent } from './assessment/assessment-search.component';
import { CreateAssessmentComponent } from './assessment/create-assessment.component';
import { EditAssessmentComponent } from './assessment/edit-assessment.component';
import { AssessmentComponent } from './assessment/assessment.component';
import { PracticeTestComponent } from './assessment/practice-test.component';

import { CreateQuestionComponent } from './question/create-question.component';
import { EditQuestionComponent } from './question/edit-question.component';

import { QuestionComponent } from './question/question.component';

import { UserProfileComponent } from './user/user-profile.component';

import { PrintAssessmentComponent } from "./assessment/print-assessment.component";

const appRoutes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'registration-confirmation', component: ConfirmEmailSentComponent },
  { path: 'register/confirm/:user_id/:token', component: ConfirmEmailConfirmedComponent },
  { path: 'print-assessment/:id', component: PrintAssessmentComponent },
  { path: '', component: FullLayoutComponent, canActivate: [AuthGuard], children: [
    { path: '', component: PortalSearchComponent },
    { path: 'portal-search', component: PortalSearchComponent },
    { path: 'create-portal', component: CreatePortalComponent },
    { path: 'edit-portal/:id', component: EditPortalComponent },
    { path: 'portal/:id', component: PortalComponent },
    { path: 'assessment-search/:id', component: AssessmentSearchComponent },
    { path: 'create-assessment/:id', component: CreateAssessmentComponent },
    { path: 'edit-assessment/:portal_id/:id', component: EditAssessmentComponent },
    { path: 'assessment/:portal_id/:id', component: AssessmentComponent },
    { path: 'create-question/:id', component: CreateQuestionComponent },
    { path: 'edit-question/:portalId/:id', component: EditQuestionComponent },
    { path: 'question/:portalId/:id', component: QuestionComponent },
    { path: 'practice/:id', component: PracticeTestComponent },
    { path: 'user-profile/:username', component: UserProfileComponent },
    { path: 'access-denied', component: AccessDeniedComponent }
  ]},
  { path: '**', redirectTo: '' }
];

export const routing = RouterModule.forRoot(appRoutes);
