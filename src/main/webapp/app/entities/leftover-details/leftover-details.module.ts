import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { InventorySharedModule } from '../../shared';
import {
    LeftoverDetailsService,
    LeftoverDetailsPopupService,
    LeftoverDetailsComponent,
    LeftoverDetailsDetailComponent,
    LeftoverDetailsDialogComponent,
    LeftoverDetailsPopupComponent,
    LeftoverDetailsDeletePopupComponent,
    LeftoverDetailsDeleteDialogComponent,
    leftoverDetailsRoute,
    leftoverDetailsPopupRoute,
} from './';

const ENTITY_STATES = [
    ...leftoverDetailsRoute,
    ...leftoverDetailsPopupRoute,
];

@NgModule({
    imports: [
        InventorySharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        LeftoverDetailsComponent,
        LeftoverDetailsDetailComponent,
        LeftoverDetailsDialogComponent,
        LeftoverDetailsDeleteDialogComponent,
        LeftoverDetailsPopupComponent,
        LeftoverDetailsDeletePopupComponent,
    ],
    entryComponents: [
        LeftoverDetailsComponent,
        LeftoverDetailsDialogComponent,
        LeftoverDetailsPopupComponent,
        LeftoverDetailsDeleteDialogComponent,
        LeftoverDetailsDeletePopupComponent,
    ],
    providers: [
        LeftoverDetailsService,
        LeftoverDetailsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InventoryLeftoverDetailsModule {}
