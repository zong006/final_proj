import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { User } from '../../model/User';
import { LoginStore } from '../../store/LoginStore';
import { SectionsService } from '../../service/user-settings/sections.service';
import { Section } from '../../model/Section';
import { db } from '../../IndexDB/indexDB';
import { UserService } from '../../service/user/user.service';
import { Router } from '@angular/router';
import { ArticleStore } from '../../store/ArticleStore';

@Component({
  selector: 'app-user-settings',
  standalone: false,
  templateUrl: './user-settings.component.html',
  styleUrl: './user-settings.component.css'
})
export class UserSettingsComponent implements OnInit{
  
  ngOnInit(): void {
    this.form = this.createForm();
    this.pushTopicsToIndexDB();

    this.loginStore.user$.subscribe( 
      (u) => {
        // topicArray.forEach(t => this.userCurrPref.add(t));
        this.user = u;
        u.selectedTopics.forEach(t => this.userCurrPref.add(t));
      }
    );
    console.info('>>> on init settings: ', this.userCurrPref);
    this.generateTopicList();
    // this.generateItems();
  }

  private loginStore = inject(LoginStore);
  private sectionSvc = inject(SectionsService);
  private userSvc = inject(UserService);
  private router = inject(Router)
  private articleStore = inject(ArticleStore)

  private fb = inject(FormBuilder);
  form !: FormGroup;

  topicList : Section[] = []; 
  // selectedTopics = new Set<string>();
  userCurrPref = new Set<string>();
  
  
  user : User = {
    id : '',
    username : '',
    displayName : '',
    selectedTopics : []
  }

  createForm() : FormGroup {
    return this.fb.group({
      selectedTopics : this.fb.array([])
    })
  }

  checkboxChange(event : any){
    console.info('>>>checkbox: ', event.target.value);
    if (event.target.checked){
      console.info('>>>check: ', event.target.value);
      this.userCurrPref.add(event.target.value);
    }
    else {
      console.info('>>> remove: ', event.target.value);
      this.userCurrPref.delete(event.target.value);
    }
    console.info('>>>selected topics: ', this.userCurrPref);
    console.info('>>> user selected topics: ', this.user.selectedTopics);
  }

  processForm(){
  
    this.user.selectedTopics = [];
    this.userCurrPref.forEach(t => {
        const ind = this.user.selectedTopics.findIndex(x => x === t);
        if ( ind == -1){
          this.user.selectedTopics.push(t);
        }
        else {
          this.user.selectedTopics.splice(ind,1);
        }
        
      }
    );
    console.info('>>>new user selected topics: ', this.user.selectedTopics);
    this.articleStore.resetCurrPage$(1)
    console.info('>>> values: ', this.user);
    this.userSvc.updateUserPref(this.user);

    window.alert("Your preferences have been updated successfully!");

    this.router.navigate(['/dashboard']);
  }

  private sections !: Section[]  ;

  private async pushTopicsToIndexDB(){

    this.sections = await this.sectionSvc.getSections()

    const s = await db.getSections();
    if (s.length === 0){
      db.addSections(this.sections);
    }
  }

  private async generateTopicList(){
    const sec = await db.getSections();
    sec.forEach(s => {
      if (s.id != "_id"){
        this.topicList.push(s);
      }
      
    })
  }
}
