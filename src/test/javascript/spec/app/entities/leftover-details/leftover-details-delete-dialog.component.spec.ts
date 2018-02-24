/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { InventoryTestModule } from '../../../test.module';
import { LeftoverDetailsDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/leftover-details/leftover-details-delete-dialog.component';
import { LeftoverDetailsService } from '../../../../../../main/webapp/app/entities/leftover-details/leftover-details.service';

describe('Component Tests', () => {

    describe('LeftoverDetails Management Delete Component', () => {
        let comp: LeftoverDetailsDeleteDialogComponent;
        let fixture: ComponentFixture<LeftoverDetailsDeleteDialogComponent>;
        let service: LeftoverDetailsService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [InventoryTestModule],
                declarations: [LeftoverDetailsDeleteDialogComponent],
                providers: [
                    LeftoverDetailsService
                ]
            })
            .overrideTemplate(LeftoverDetailsDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(LeftoverDetailsDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LeftoverDetailsService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
