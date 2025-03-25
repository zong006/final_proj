import { Component, inject, OnInit } from '@angular/core';
import { LoginService } from '../../service/login/login.service';
import { LeaderboardService } from '../../service/leaderboard/leaderboard.service';
import { LoginStore } from '../../store/LoginStore';


@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  

  private loginSvc = inject(LoginService);
  

  login(){
    this.loginSvc.loginWithGoogle();
  }

  logout(){
    this.loginSvc.logout()
  }
}
