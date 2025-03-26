import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { lastValueFrom } from 'rxjs';
import { Section } from '../../model/Section';

@Injectable({
  providedIn: 'root'
})
export class SectionsService {

  constructor(private httpClient : HttpClient) { }

  private sectionsURL = "https://finalproj-production-3955.up.railway.app";

  getSections(){
    // return lastValueFrom(this.httpClient.get<Section[]>(this.sectionsURL + '/articles/sections'));
    return lastValueFrom(this.httpClient.get<Section[]>('/api/articles/sections'));
    // .subscribe(
    //   (response) => {
    //     console.info('>>> sections: ', response);
    //   }
    // );
  }
}
