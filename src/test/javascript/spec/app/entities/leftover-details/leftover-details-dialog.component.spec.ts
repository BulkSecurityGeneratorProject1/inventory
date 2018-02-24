/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { InventoryTestModule } from '../../../test.module';
import { LeftoverDetailsDialogComponent } from '../../../../../../main/webapp/app/entities/leftover-details/leftover-details-dialog.component';
import { LeftoverDetailsService } from '../../../../../../main/webapp/app/entities/leftover-details/leftover-details.service';
import { LeftoverDetails } from '../../../../../../main/webapp/app/entities/leftover-details/leftover-details.model';
import { ProductDetailsService } from '../../../../../../main/webapp/app/entities/product-details';

describe('Component Tests', () => {

    describe('LeftoverDetails Management Dialog Component', () => {
        let comp: LeftoverDetailsDialogComponent;
        let fixture: ComponentFixture<LeftoverDetailsDialogComponent>;
        let service: LeftoverDetailsService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [InventoryTestModule],
                declarations: [LeftoverDetailsDialogComponent],
                providers: [
                    ProductDetailsService,
                    LeftoverDetailsService
                ]
            })
            .overrideTemplate(LeftoverDetailsDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(LeftoverDetailsDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LeftoverDetailsService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new LeftoverDetails(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.leftoverDetails = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'leftoverDetailsListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new LeftoverDetails();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.leftoverDetails = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'leftoverDetailsListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
