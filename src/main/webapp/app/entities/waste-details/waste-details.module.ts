import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { InventorySharedModule } from '../../shared';
import {
    WasteDetailsService,
    WasteDetailsPopupService,
    WasteDetailsComponent,
    WasteDetailsDetailComponent,
    WasteDetailsDialogComponent,
    WasteDetailsPopupComponent,
    WasteDetailsDeletePopupComponent,
    WasteDetailsDeleteDialogComponent,
    wasteDetailsRoute,
    wasteDetailsPopupRoute,
} from './';

const ENTITY_STATES = [
    ...wasteDetailsRoute,
    ...wasteDetailsPopupRoute,
];

@NgModule({
    imports: [
        InventorySharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        WasteDetailsComponent,
        WasteDetailsDetailComponent,
        WasteDetailsDialogComponent,
        WasteDetailsDeleteDialogComponent,
        WasteDetailsPopupComponent,
        WasteDetailsDeletePopupComponent,
    ],
    entryComponents: [
        WasteDetailsComponent,
        WasteDetailsDialogComponent,
        WasteDetailsPopupComponent,
        WasteDetailsDeleteDialogComponent,
        WasteDetailsDeletePopupComponent,
    ],
    providers: [
        WasteDetailsService,
        WasteDetailsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InventoryWasteDetailsModule {}
