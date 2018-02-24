import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { LeftoverDetailsComponent } from './leftover-details.component';
import { LeftoverDetailsDetailComponent } from './leftover-details-detail.component';
import { LeftoverDetailsPopupComponent } from './leftover-details-dialog.component';
import { LeftoverDetailsDeletePopupComponent } from './leftover-details-delete-dialog.component';

export const leftoverDetailsRoute: Routes = [
    {
        path: 'leftover-details',
        component: LeftoverDetailsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'inventoryApp.leftoverDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'leftover-details/:id',
        component: LeftoverDetailsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'inventoryApp.leftoverDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const leftoverDetailsPopupRoute: Routes = [
    {
        path: 'leftover-details-new',
        component: LeftoverDetailsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'inventoryApp.leftoverDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'leftover-details/:id/edit',
        component: LeftoverDetailsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'inventoryApp.leftoverDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'leftover-details/:id/delete',
        component: LeftoverDetailsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'inventoryApp.leftoverDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
