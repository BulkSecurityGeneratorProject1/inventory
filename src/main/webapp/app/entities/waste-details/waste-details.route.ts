import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { WasteDetailsComponent } from './waste-details.component';
import { WasteDetailsDetailComponent } from './waste-details-detail.component';
import { WasteDetailsPopupComponent } from './waste-details-dialog.component';
import { WasteDetailsDeletePopupComponent } from './waste-details-delete-dialog.component';

export const wasteDetailsRoute: Routes = [
    {
        path: 'waste-details',
        component: WasteDetailsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'inventoryApp.wasteDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'waste-details/:id',
        component: WasteDetailsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'inventoryApp.wasteDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const wasteDetailsPopupRoute: Routes = [
    {
        path: 'waste-details-new',
        component: WasteDetailsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'inventoryApp.wasteDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'waste-details/:id/edit',
        component: WasteDetailsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'inventoryApp.wasteDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'waste-details/:id/delete',
        component: WasteDetailsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'inventoryApp.wasteDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
