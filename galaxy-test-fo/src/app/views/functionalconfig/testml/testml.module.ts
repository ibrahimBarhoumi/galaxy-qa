import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
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
import {DragDropModule} from '@angular/cdk/drag-drop';
import { TestMLRoutingModule } from './testml.routing';
import {TranslateModule} from '@ngx-translate/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { ModalTestMLComponent } from './modal-testml/modal-testml.component';
import { ListTestMLComponent } from './list-testml/list-testml.component';
import { TestService } from '../../test/test.service';

@NgModule({
  declarations: [ListTestMLComponent, ModalTestMLComponent],
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
    TestMLRoutingModule,
    TranslateModule,
    NgSelectModule,
  ],
  providers: [TestService],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TestMLModule {}
