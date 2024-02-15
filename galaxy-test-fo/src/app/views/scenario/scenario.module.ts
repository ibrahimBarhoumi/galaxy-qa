import { NgModule , CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import { CommonModule } from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {TableModule} from 'primeng/table';
import {ToastModule} from 'primeng/toast';
import {CalendarModule} from 'primeng/calendar';
import {SliderModule} from 'primeng/slider';
import {MultiSelectModule} from 'primeng/multiselect';
import {ContextMenuModule} from 'primeng/contextmenu';
import {DialogModule} from 'primeng/dialog';
import {ButtonModule} from 'primeng/button';
import {DropdownModule} from 'primeng/dropdown';
import {ProgressBarModule} from 'primeng/progressbar';
import {InputTextModule} from 'primeng/inputtext';
import { PopoverModule } from 'ngx-bootstrap/popover';
import { ModalModule } from 'ngx-bootstrap/modal';
import {MatSlideToggleModule} from '@angular/material/slide-toggle';
import {MatRadioModule} from '@angular/material/radio';
import {MatCardModule} from '@angular/material/card';
import {MatSelectModule} from '@angular/material/select';
import {PanelModule} from 'primeng/panel';
import {MatInputModule} from '@angular/material/input';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {ScenarioService} from '../scenario/scenario.service';
import {DragDropModule} from '@angular/cdk/drag-drop';
import {HttpClient} from '@angular/common/http';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {TranslateModule} from '@ngx-translate/core';
import { ScenarioRoutingModule } from './scenario.routing';
import { ListScenarioComponent } from './list-scenario/scenario-list.component';
import { NgSelectModule } from '@ng-select/ng-select';
import { CreateScenarioComponent } from './create-scenario/create-scenario.component';
import { ScenarioTestService } from './scenarioTest.service';
import { ScenarioVersionService } from './scenarioversio.service';
import { TestVersionService } from './sceraniro-test.service';

export function createTranslateLoader(http: HttpClient): any {
  return new TranslateHttpLoader(http, 'assets/i18n/', '.json');
}
@NgModule({
  declarations: [ListScenarioComponent, CreateScenarioComponent],
  imports: [
    CommonModule,
    TableModule,
    CalendarModule,
    SliderModule,
    DialogModule,
    MultiSelectModule,
    ContextMenuModule,
    DropdownModule,
    ButtonModule,
    ToastModule,
    InputTextModule,
    ProgressBarModule,
    FormsModule,
    ScenarioRoutingModule,
    TableModule,
    PopoverModule.forRoot(),
    ModalModule.forRoot(),
    ReactiveFormsModule,
    MatSlideToggleModule,
    MatRadioModule,
    MatCardModule,
    MatSelectModule,
    PanelModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule,
    DragDropModule,
    TranslateModule,
    NgSelectModule,
  ],
  providers: [ScenarioService , ScenarioTestService , ScenarioVersionService , TestVersionService],

  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ScenarioModule {}
