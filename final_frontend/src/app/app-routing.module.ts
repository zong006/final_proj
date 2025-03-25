import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { UserSettingsComponent } from './components/user-settings/user-settings.component';
import { RouteGuardService } from './service/route-guard.service';
import { LeaderboardComponent } from './components/leaderboard/leaderboard.component';

const routes: Routes = [
  {path:'', component:LoginComponent},
  {path:'dashboard', component:DashboardComponent, canActivate:[RouteGuardService]},
  {path:'settings', component:UserSettingsComponent, canActivate:[RouteGuardService]},
  {path:'leaderboard', component:LeaderboardComponent, canActivate:[RouteGuardService]} // <-- add parametrized routes for specific user
  // {path:'**', component:LeaderboardComponent} // <-- for error or default pages
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
