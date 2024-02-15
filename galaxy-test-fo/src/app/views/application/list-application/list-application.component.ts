import { Component, Input, OnInit } from '@angular/core';
import { Pageable } from '../../../shared/models/pageable';
import { LazyLoadEvent } from 'primeng/api';
import { PageOptions } from '../../../shared/config/page/pageOptions';
import { FilterOptions } from '../../../shared/config/page/filterOptions';
import { Application } from '../../../shared/models/application';
import { ApplicationService } from '../application.service';
import { ApplicationStatus, ApplicationStatus2Label } from '../../../shared/enums/applicationStatus.enum';
import { ApplicationVersionStatus, ApplicationVersionStatus2Label } from '../../../shared/enums/applicationVersionStatus.enum';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { CreateApplicationComponent } from '../create-application/create-application.component';
import { Observable } from 'rxjs';
import { ConfigurationML } from '../../../shared/models/configurationML';
import { CookieService } from 'ngx-cookie-service';
import { Router } from '@angular/router';
import { ApplicationVersion } from '../../../shared/models/application-version';
import { VersionService } from '../version/version.service';


@Component({
  templateUrl: 'list-application.component.html',
  styleUrls: ['./list-application.component.scss'],
})
export class ListApplicationComponent implements OnInit {
  bsModalRef: BsModalRef;
  application: Application[];
  data: Pageable<Application>;
  versions: ApplicationVersion[];
  cols: any[];
  public ApplicationVersionStatusLabel = ApplicationVersionStatus2Label;
  public ApplicationStatusLabel = ApplicationStatus2Label;
  public applicationStatusList = Object.values(ApplicationStatus);
  deleted = false
  duplicateMode = true
  hasVersions = [];
  constructor(
    private applicationService: ApplicationService,
    private modalService: BsModalService,
    private cookiesService: CookieService,
    private applicationVersionService: VersionService,
  ) {}

  ngOnInit(): void {
    this.cols = [
      { field: 'code', header: 'APPLICATION.LIST.COLS.CODE' , type: 'text', hasConfiguration: false , hasUser:false },
      { field: 'applicationStatus', header: 'APPLICATION.LIST.COLS.STATUS' , type: 'text', hasConfiguration: false, style: 'badge',hasUser:false  },
      { field: 'creationDate', header: 'APPLICATION.LIST.COLS.CREATIONDATE'  , type: 'date', hasConfiguration: false , hasUser:false },
      { field: 'fullName', header: 'APPLICATION.LIST.COLS.FULLNAME'  , type: 'text', hasConfiguration: false , hasUser: true },
      { field: 'comment', header: 'APPLICATION.LIST.COLS.COMMENT'  , type: 'text', hasConfiguration: true , hasUser:false },
      { field: 'label', header: 'APPLICATION.LIST.COLS.LABEL'  , type: 'text', hasConfiguration: true , hasUser:false },
    ];
    let state = JSON.parse(localStorage.getItem("stateApplicationDataTable"));
    if(state){
    delete state?.expandedRowKeys;
    localStorage.setItem("stateApplicationDataTable",JSON.stringify(state));
  }
  }

  list(event?: LazyLoadEvent): void {
    this.data = null;
    const state = JSON.parse(localStorage.getItem('stateApplicationDataTable'));
    const page = Math.floor(event?.first / event?.rows) + 1 || Math.floor(state?.first / state?.rows) + 1 || 1;
    const size = event?.rows || state?.rows ||  10;
    const filters = event?.filters || state?.filters || {};
    this.applicationService.list(new PageOptions( page - 1, size ), new FilterOptions( filters )).subscribe((data) => {
      this.data = data ;
    });
  }
  openModalWithComponent(application?:Application , duplicateMode?: boolean ) {
     let exist: boolean
     let deleted: boolean
     this.duplicateMode = true
    this.bsModalRef = this.modalService.show(CreateApplicationComponent ,{initialState: { application, exist , duplicateMode , deleted}});
    this.bsModalRef.content.event.subscribe((res: Observable<Application>) => {
    res.subscribe(() => {
        this.bsModalRef.hide();
        this.list();
      },  (err) => {
        if (err.error.message == "APPLICATION_CODE_EXISTS") {
          exist = true;
          this.bsModalRef.content.exist = exist ;
        }
        if (err.error.message == "APPLICATION_CODE_DELETED"){
          deleted = true
          this.bsModalRef.content.deleted = deleted
        }
      });

   });
  }
  selectConfiguration(configurationMLS: ConfigurationML[]) {
    return configurationMLS.find(x => x.languageCode === this.cookiesService.get("lang")) || {};
  }
  delete(id: string ){
    this.applicationService.delete(id).subscribe(()=>{
      this.list();
    },(err)=>{
      if(err.error.message == 'APPLICATION_HAS_VERSIONS'){
        this.hasVersions[id] = true;
      }}
      );
    }

  getVersions(id:string){
    return this.applicationVersionService.getVersionsByApplicationId(id).subscribe(data=>{
      this.versions = data.content;
    });
  }
}
