import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { SalesDetailsComponent } from './sales-details.component';
import { SalesDetailsDetailComponent } from './sales-details-detail.component';
import { SalesDetailsPopupComponent } from './sales-details-dialog.component';
import { SalesDetailsDeletePopupComponent } from './sales-details-delete-dialog.component';

export const salesDetailsRoute: Routes = [
    {
        path: 'sales-details',
        component: SalesDetailsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'inventoryApp.salesDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'sales-details/:id',
        component: SalesDetailsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'inventoryApp.salesDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const salesDetailsPopupRoute: Routes = [
    {
        path: 'sales-details-new',
        component: SalesDetailsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'inventoryApp.salesDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sales-details/:id/edit',
        component: SalesDetailsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'inventoryApp.salesDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sales-details/:id/delete',
        component: SalesDetailsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'inventoryApp.salesDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
