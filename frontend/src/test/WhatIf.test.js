// eslint-disable-next-line import/no-extraneous-dependencies
const { Builder, By, until } = require('selenium-webdriver');
// eslint-disable-next-line import/no-extraneous-dependencies
const chrome = require('selenium-webdriver/chrome');

// eslint-disable-next-line no-undef
jest.setTimeout(30000); // increase timeout for slow browsers

// eslint-disable-next-line no-undef
describe('WhatIf UI (Selenium)', () => {
  let driver;

  // eslint-disable-next-line no-undef
  beforeAll(async () => {
    const options = new chrome.Options();
    options.addArguments('--headless');
    options.addArguments('--no-sandbox');
    options.addArguments('--disable-dev-shm-usage');

    driver = await new Builder().forBrowser('chrome').setChromeOptions(options).build();
    await driver.get('http://localhost:3000/what-if'); // Ensure frontend is running
  });

  // eslint-disable-next-line no-undef
  afterAll(async () => {
    await driver.quit();
  });

  // eslint-disable-next-line no-undef
  test('select the button and run', async () => {
    const courseInput = await driver.findElement(By.name('degreeName'));
    await courseInput.click();

    const termSelect = await driver.findElement(By.text('BTec1-Automt Sys EngTech CO-OP'));
    await termSelect.click();
    const emailInput = await driver.findElement(By.class('submission-btn'));
    await emailInput.click();

    const successBox = await driver.wait(
      until.elementLocated(By.class('component-container')),
      10000
    );
  });
});
