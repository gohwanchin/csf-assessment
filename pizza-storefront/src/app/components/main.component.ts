import { Component, OnInit, Output } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormControl, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { Order } from '../models';
import { PizzaService } from '../pizza.service';

const SIZES: string[] = [
  "Personal - 6 inches",
  "Regular - 9 inches",
  "Large - 12 inches",
  "Extra Large - 15 inches"
]

const PizzaToppings: string[] = [
    'chicken', 'seafood', 'beef', 'vegetables',
    'cheese', 'arugula', 'pineapple'
]

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {

  pizzaSize = SIZES[0]

  form!: FormGroup
  toppingsArr!: FormArray

  constructor(private fb: FormBuilder, private pizzaSvc: PizzaService, private router: Router) {}

  ngOnInit(): void {
    this.form = this.createForm()
  }

  updateSize(size: string) {
    this.pizzaSize = SIZES[parseInt(size)]
  }

  process(){
    const order: Order = this.form.value as Order
    const selectedToppings = order.toppings
        .map((checked: boolean, i: number) => checked ? PizzaToppings[i]: null)
        .filter((v: any) => v!== null)
    order.toppings = selectedToppings
    console.log(order);
    this.pizzaSvc.createOrder(order)
        .then(result => {
          console.log(result);
          this.router.navigate(['/orders',order.email])
        }).catch(error => {
          console.error(error);
        })
    this.form = this.createForm()
  }

  list(){
    const email = this.form.value.email
    console.log(email);
    this.router.navigate(['/orders',email])
  }

  private createForm() {
    this.toppingsArr = this.fb.array([], this.minSelectedCheckboxes(1))
    this.addToppings()
    return this.fb.group({
      name: this.fb.control('', Validators.required),
      email: this.fb.control('', [Validators.required, Validators.email]),
      size: this.fb.control(0, Validators.required),
      base: this.fb.control('', Validators.required),
      sauce: this.fb.control('', Validators.required),
      toppings: this.toppingsArr,
      comments: this.fb.control('')
    })
  }

  private addToppings() {
    PizzaToppings.forEach(() => this.toppingsArr.push(new FormControl(false)))
  }

  minSelectedCheckboxes(min = 1) {
    const validator: ValidatorFn = (formArray: AbstractControl) => {
      if (formArray instanceof FormArray) {
        const totalSelected = formArray.controls
          .map((control) => control.value)
          .reduce((prev, next) => (next ? prev + next : prev), 0);
        return totalSelected >= min ? null : { required: true };
      }
      throw new Error('formArray is not an instance of FormArray');
    };
    return validator;
  }
}
