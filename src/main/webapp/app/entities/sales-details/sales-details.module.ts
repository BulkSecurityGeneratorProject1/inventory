import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { InventorySharedModule } from '../../shared';
import {
    SalesDetailsService,
    SalesDetailsPopupService,
    SalesDetailsComponent,
    SalesDetailsDetailComponent,
    SalesDetailsDialogComponent,
    SalesDetailsPopupComponent,
    SalesDetailsDeletePopupComponent,
    SalesDetailsDeleteDialogComponent,
    salesDetailsRoute,
    salesDetailsPopupRoute,
} from './';

const ENTITY_STATES = [
    ...salesDetailsRoute,
    ...salesDetailsPopupRoute,
];

@NgModule({
    imports: [
        InventorySharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        SalesDetailsComponent,
        SalesDetailsDetailComponent,
        SalesDetailsDialogComponent,
        SalesDetailsDeleteDialogComponent,
        SalesDetailsPopupComponent,
        SalesDetailsDeletePopupComponent,
    ],
    entryComponents: [
        SalesDetailsComponent,
        SalesDetailsDialogComponent,
        SalesDetailsPopupComponent,
        SalesDetailsDeleteDialogComponent,
        SalesDetailsDeletePopupComponent,
    ],
    providers: [
        SalesDetailsService,
        SalesDetailsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InventorySalesDetailsModule {}
