import { Injectable } from "@angular/core";
import { ComponentStore } from "@ngrx/component-store";
import { User } from "../model/User";

export interface LoginState{
    loggedIn : boolean;
    user : User;
    dooomScrollScore : number;
    delta : number
}

@Injectable({
    providedIn: 'root'
})

export class LoginStore  extends ComponentStore<LoginState>{
    constructor(){ 
        super({loggedIn: false,
            user : {
                username : '',
                id : '',
                displayName : '',
                selectedTopics : []
            },
            dooomScrollScore : 0,
            delta :0
        });
    }

    readonly loggedIn$ = this.select(currState => currState.loggedIn); 
    readonly user$ = this.select(currState => currState.user);
    readonly doomScrollScore$ = this.select(currState => currState.dooomScrollScore);
    readonly delta$ = this.select(currState => currState.delta);

    //============================================================================== // 

    readonly setUser$ = this.updater<User>(
        (currState : LoginState, user : User) => {
            const newState : LoginState = {
                loggedIn : currState.loggedIn,
                user : user,
                dooomScrollScore : currState.dooomScrollScore,
                delta : currState.delta
            }
            return newState;
        }
    )


    readonly setLoggedIn$ = this.updater<boolean>( 
        (currState: LoginState, newLoggedInStatus : boolean) => {
            const newState : LoginState = {
                loggedIn : newLoggedInStatus,
                user : currState.user,
                dooomScrollScore : currState.dooomScrollScore,
                delta : currState.delta
            }
            return newState;
        }
    )

    readonly incrDoomScrollStoreDelta$ = this.updater<number>( 
        (currState: LoginState, toIncr : number) => {
            const newState : LoginState = {
                loggedIn : currState.loggedIn,
                user : currState.user,
                dooomScrollScore : currState.dooomScrollScore + toIncr,
                delta : currState.delta + toIncr
            }
            return newState;
        }
    )

    readonly setDoomScrollScore$ = this.updater<number>(
        (currState: LoginState, score : number) => {
            const newState : LoginState = {
                loggedIn : currState.loggedIn,
                user : currState.user,
                dooomScrollScore : score,
                delta : currState.delta
            }
            return newState;
        }
    )

    readonly resetDoomScrollStoreDelta$ = this.updater<number>(
        (currState: LoginState, zero : number) => {
            const newState : LoginState = {
                loggedIn : currState.loggedIn,
                user : currState.user,
                dooomScrollScore : currState.dooomScrollScore,
                delta : zero
            }
            return newState;
        }
    )
}



