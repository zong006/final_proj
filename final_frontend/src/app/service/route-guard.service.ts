import { Injectable, inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, GuardResult, MaybeAsync, Router, RouterStateSnapshot } from '@angular/router';
import { LoginStore } from '../store/LoginStore';
import { firstValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RouteGuardService implements CanActivate{

  constructor(private router : Router) { }

  private loginStore = inject(LoginStore);

  
  async canActivate(){

    const isLoggedIn = await firstValueFrom(this.loginStore.loggedIn$);
    
    if (!isLoggedIn){
      console.log('>>> not logged in ')
      alert('Please login or signup..');
      this.router.navigate(['']);
    }
    
    console.info('>>>route guard: ', isLoggedIn);
    return isLoggedIn;
  }
}
