/**
 * @fileoverview transpiled from org.dominokit.samples.App.
 *
 * @suppress {const, extraRequire, missingOverride, missingRequire, suspiciousCode, transitionalSuspiciousCodeWarnings, unusedLocalVariables, uselessCode}
 */
goog.module('org.dominokit.samples.App$impl');


const EntryPoint = goog.require('com.google.gwt.core.client.EntryPoint$impl');
const j_l_Object = goog.require('java.lang.Object$impl');
const $Util = goog.require('nativebootstrap.Util$impl');

let Map = goog.forwardDeclare('java.util.Map$impl');
let TreeMap = goog.forwardDeclare('java.util.TreeMap$impl');
let DominoDo = goog.forwardDeclare('org.dominokit.samples.DominoDo$impl');


/**
 * @implements {EntryPoint}
  */
class App extends j_l_Object {
  /**
   * @protected
   */
  constructor() {
    super();
    /** @public {Map<?string, ?string>} */
    this.f_map__org_dominokit_samples_App_;
  }
  
  /**
   * @return {!App}
   * @public
   */
  static $create__() {
    App.$clinit();
    let $instance = new App();
    $instance.$ctor__org_dominokit_samples_App__();
    return $instance;
  }
  
  /**
   * @return {void}
   * @public
   */
  $ctor__org_dominokit_samples_App__() {
    this.$ctor__java_lang_Object__();
    this.$init__org_dominokit_samples_App();
  }
  
  /**
   * @override
   * @return {void}
   * @public
   */
  m_onModuleLoad__() {
    this.f_map__org_dominokit_samples_App_.put("x", "y");
    this.f_map__org_dominokit_samples_App_.remove("x");
    this.f_map__org_dominokit_samples_App_.values().m_remove__java_lang_Object("x");
    DominoDo.$create__().m_run__();
  }
  
  /**
   * @return {void}
   * @private
   */
  $init__org_dominokit_samples_App() {
    this.f_map__org_dominokit_samples_App_ = /**@type {!TreeMap<?string, ?string>} */ (TreeMap.$create__());
  }
  
  /**
   * @return {void}
   * @public
   */
  static $clinit() {
    App.$clinit = (() =>{
    });
    App.$loadModules();
    j_l_Object.$clinit();
  }
  
  /**
   * @param {?} instance
   * @return {boolean}
   * @public
   */
  static $isInstance(instance) {
    return instance instanceof App;
  }
  
  /**
   * @param {Function} classConstructor
   * @return {boolean}
   * @public
   */
  static $isAssignableFrom(classConstructor) {
    return $Util.$canCastClass(classConstructor, App);
  }
  
  /**
   * @public
   */
  static $loadModules() {
    TreeMap = goog.module.get('java.util.TreeMap$impl');
    DominoDo = goog.module.get('org.dominokit.samples.DominoDo$impl');
  }
  
  
};

$Util.$setClassMetadata(App, $Util.$makeClassName('org.dominokit.samples.App'));


EntryPoint.$markImplementor(App);


/* NATIVE.JS EPILOG */

const org_dominokit_samples_App = App;

/*
 * #%L
 * Connected
 * %%
 * Copyright (C) 2017 Vertispan
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

// Defer this command, since this will be folded into the FlowChartEntryPoint js impl,
// and if it runs right away, will not have its dependencies resolved yet (at least while
// running in BUNDLE).
setTimeout(function(){
  // Call the java "constructor" method, `new` will only work if it is a @JsType, or maybe
  // once optimized. Without this, in BUNDLE mode, `new` doesn't include the clinit, so
  // static imports haven't been resolved yet.
  var ep = App.$create__();
  // Invoke onModuleLoad to start the app.
  ep.m_onModuleLoad__()
}, 0);



exports = App; 
//# sourceMappingURL=App.js.map