import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { OrderSummary } from '../models';
import { PizzaService } from '../pizza.service';

@Component({
  selector: 'app-order',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css']
})
export class OrdersComponent implements OnInit {

  orders: Array<OrderSummary> = []
  email!: string
  
  constructor(private route: ActivatedRoute, private pizzaSvc: PizzaService) { }

  ngOnInit(): void {
    this.email = this.route.snapshot.params['email']
    this.pizzaSvc.getOrders(this.email).then(result => {
      console.log(result)
      this.orders = result
    }).catch(error => {
      console.error(error);
    })
  }

}
